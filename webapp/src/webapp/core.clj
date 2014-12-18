(ns webapp.core
  (:refer-clojure :exclude [sort find])
  (:require (compojure handler route core response)
  		[ring.util.response :as response]
  		[ring.middleware.params :refer :all]
  		[webapp.ff_home :refer :all]
  		[webapp.html_helpers :refer :all]
  		[webapp.html_post :refer :all]
  		[webapp.microblog_home :refer :all]
  		[webapp.logging :refer :all]
  		[webapp.site_stats :refer :all]
  		[compojure.core :refer :all]
  		[compojure.response :refer :all]
  		[compojure.handler :refer :all]
        [ring.adapter.jetty :only (run-jetty)]
        [cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])
        [hiccup.core :refer :all]))

(def site-map 
	(str site_header (html
		[:body 
			[:h2 "Doug's Website"]
			[:div {:class "main" :style "padding-bottom:5px"}
				[:p {:style "padding:15px;padding-bottom:0px"} [:b "Pages:"]]
				[:ul (linkfy "microblog" "Micro-Blog")]
				[:ul (linkfy "ff" "Fantasy Football")]
				[:ul "Full Blog"]
				[:ul "Poetry Reader"]
				[:ul (linkfy "site_stats" "Site Statistics")]
		]])))

(def users {"root" {:username "root"
                    :password (creds/hash-bcrypt "admin_password")
                    :roles #{::admin}}
            "jane" {:username "jane"
                    :password (creds/hash-bcrypt "user_password")
                    :roles #{::user}}})

(defroutes router*
	(GET "/" request (do (log request "homepage" "page-view" (:remote-addr request) {}) site-map))
	(GET "/ff" request (do (log request "fantasy-football" "page-view" (:remote-addr request) {}) ff))
	(GET "/microblog" request (do (log request "microblog" "page-view" (:remote-addr request) {}) (microblog)))
	(GET "/microblog/post" request (friend/authenticated (if true (do (log request "microblog_post" "page-view" (:remote-addr request) {}) (mb_post))(str "Try again!\n" request))))
    (POST "/microblog_post" request (let [message (:message (:params request))] (do (log request "microblog_post" "publish" (:remote-addr request) {:message message}) (mb_post_post message))))
    (GET "/site_stats" request (do (log request "site_stats" "page-view" (:remote-addr request) {}) (stats-output)))    
	(compojure.route/not-found "Link not found."))

; No Authentication:
; (def router (compojure.handler/api router*))

(def secured-app (friend/authenticate
	router*
		{:allow-anon? true
		:unauthenticated-handler #(workflows/http-basic-deny "Friend demo" %)
		:workflows [(workflows/http-basic
		:credential-fn #(creds/bcrypt-credential-fn users %)
		:realm "Friend demo")]}))

(def router (site secured-app))