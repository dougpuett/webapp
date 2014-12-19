(ns webapp.core
	(:refer-clojure :exclude [sort find])
	(:import org.mindrot.jbcrypt.BCrypt)
	(:require (compojure handler route core response)
			[ring.util.response :as response]
			[ring.middleware.params :refer :all]
			[webapp.controllers :refer :all]
			[compojure.core :refer :all]
			[compojure.response :refer :all]
			[compojure.handler :refer :all]
			[taoensso.carmine :as car :refer (wcar)]
			[ring.adapter.jetty :only (run-jetty)]
			[cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])))

(defroutes router*
	(GET "/" [] homepage_ctrl)
	(GET "/ff" [] fantasy-football_ctrl)
	(GET "/microblog" [] microblog_ctrl)
	(GET "/microblog/post" [] microblog_post_ctrl)
	(POST "/microblog_post" [] microblog_publish_ctrl)
	(GET "/site_stats" [] site_stats_ctrl)
	(compojure.route/not-found "Link not found."))

; No Authentication:
; (def router (compojure.handler/api router*))

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
		:unauthenticated-handler #(workflows/http-basic-deny "Login to post" %)
		:workflows [(workflows/http-basic
		:credential-fn redis-credentials
		:realm "Login to Post")]}))

(def router (site secured-app))