(ns webapp.core
  (:refer-clojure :exclude [sort find])
  (:require (compojure handler route route core)
  		[ring.util.response :as response]
  		[ring.middleware.params :refer :all]
  		[webapp.ff_home :refer :all]
  		[webapp.html_helpers :refer :all]
  		[webapp.html_post :refer :all]
  		[webapp.microblog_home :refer :all]
  		[webapp.logging :refer :all]
  		[webapp.site_stats :refer :all]
  		[compojure.core :refer :all]; :only (GET POST defroutes context)]
        [ring.adapter.jetty :only (run-jetty)]
        [hiccup.core :refer :all]))

(def site-map 
	(str microblog_header (html
		[:body {:style "background-color:#99CCFF;background: linear-gradient(to right, #99CCFF, #8AB8E6, #99CCFF);margin-top:10px"}
			[:h2 {:style "margin-left:50px;padding-left:15px;background-color:white;width:485px;padding-top:14px;padding-bottom:14px;border-radius:6px"} "Doug's Website"]
			[:div {:style "margin-left:50px;padding-left:15px;background-color:white;width:485px;padding-top:14px;padding-bottom:14px;border-radius:6px"}
				[:p [:b "Pages:"]]
				[:ul (linkfy "microblog" "Micro-Blog")]
				[:ul (linkfy "ff" "Fantasy Football")]
				[:ul "Full Blog"]
				[:ul "Poetry Reader"]
				[:ul (linkfy "site_stats" "Site Statistics")]
		]])))

(defroutes router*
	(GET "/" request (do (log request "homepage" "page-view" (:remote-addr request) {}) site-map))
	(GET "/ff" request (do (log request "fantasy-football" "page-view" (:remote-addr request) {}) ff))
	(GET "/microblog" request (do (log request "microblog" "page-view" (:remote-addr request) {}) (microblog)))
	(GET "/microblog/post" request (do (log request "microblog_post" "page-view" (:remote-addr request) {}) (mb_post)))
    (POST "/microblog_post" request (let [message (:message (:params request))] (do (log request "microblog_post" "publish" (:remote-addr request) {:message message}) (mb_post_post message))))
    (GET "/site_stats" request (do (log request "site_stats" "page-view" (:remote-addr request) {}) (stats-output)))    
	(compojure.route/not-found "Link not found."))

(def router (compojure.handler/api router*))