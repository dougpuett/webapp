(ns webapp.core
	(:refer-clojure :exclude [find])
	(:import org.mindrot.jbcrypt.BCrypt)
	(:import (java.io ByteArrayOutputStream))
	(:require (compojure handler route core response)
			[ring.util.response :as response]
			[ring.middleware.params :refer :all]
			[webapp.controllers :refer :all]
			[webapp.beowulf :refer :all]
			[compojure.core :refer :all]
			[compojure.response :refer :all]
			[compojure.handler :refer :all]
			[taoensso.carmine :as car :refer (wcar)]
			[ring.adapter.jetty :only (run-jetty)]
			[cognitect.transit :as transit]
			[cheshire.core :refer :all]
			[clojure.java.io :as io]
			[cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])))

(def full_input (into [] (map parse-string (clojure.string/split (slurp (io/file (io/resource "fullmap.json"))) #"\n"))))

(def output2 (into [] (flatten (for [[team weeks] (group-by #(get % "team") full_input)] 
	(let [sorted-weeks (sort-by #(get % "week") weeks)]
		(map #(hash-map 
			"team" team 
			"x1" (get %1 "week") 
			"y1" (get %1 "odds") 
			"x2" (get %2 "week") 
			"y2" (get %2 "odds")) 
		sorted-weeks (rest sorted-weeks)))))))

(def output3 (vec (concat output2 (take 25 output2))))

(defn write [x]
  (let [baos (ByteArrayOutputStream.)
        w    (transit/writer baos :json)
        _    (transit/write w x)
        ret  (.toString baos)]
    (.reset baos)
    ret))

(defn transit [req]
	{:header {"Content-Type" "application/transit+json"}
	:body (write full_input)})

(defn transit2 [req]
	{:header {"Content-Type" "application/transit+json"}
	:body (write output3)})

(defroutes router*
	(GET "/" [] homepage_ctrl)
	(GET "/login" [] login-form)
	(GET "/logout" request (friend/logout* (response/redirect (str (:context request) "/"))))
	(GET "/ff" [] fantasy-football_ctrl)
	(GET "/microblog" [] microblog_ctrl)
	(GET "/microblog/post" [] microblog_post_ctrl)
	(POST "/microblog_post" [] microblog_publish_ctrl)
	(POST "/microblog_post_content" req (do (microblog_publish_ctrl req) (response/redirect "/content")))
	(GET "/site_stats" [] site_stats_ctrl)
	(GET "/ff_graph" [] d3_test)
	(GET "/ff_data" [] transit)
	(GET "/ff_data2" [] transit2)
	(GET "/content" [] (friend/authenticated (content)))
	; (GET "/request" [] str)
	(GET "/requires-authentication" req
		(friend/authenticated "Thanks for authenticating!"))
	; (GET "/role-user" req
	;	(friend/authorize #{:webapp.core/user :webapp.core/admin} "You're a user!"))
	; (GET "/role-admin" req
	; 	(friend/authorize #{:webapp.core/admin} "You're an admin!"))
	(compojure.route/resources "/")
	(compojure.route/not-found "Link not found."))

;; =====================
;; REDIS AUTHENTICATION:
;; =====================
(def server1-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))
(defn get_roles_from_redis [username] (wcar* (car/get (str "password:" username))))

(defn redis-credentials
	"This will be a function that takes a map {:username X :password Y} and returns {:username X :roles Z} iff hash(Y) = Y' (hashed password in redis)."
		[{:keys [username password]}]
		(let [{:keys [roles hashed-password]} (get_roles_from_redis username)]
			(if 
				(nil? hashed-password)
				nil
					(if 
						(BCrypt/checkpw password hashed-password)
						{:username username :roles roles}
						nil))))

(def secured-app (friend/authenticate
	router*
		{:allow-anon? true
		:login-uri "/login"
		:default-landing-uri "/"
		:unauthorized-handler #(-> (str "You do not have sufficient privileges to access" (:uri %))
			response/response
			(response/status 401))
		:credential-fn redis-credentials
		:workflows [(workflows/interactive-form)]}))

(def router (site secured-app))