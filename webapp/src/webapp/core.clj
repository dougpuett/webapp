(ns webapp.core
  (:refer-clojure :exclude [sort find])
  (:require (compojure handler route route core)
  		[ring.util.response :as response]
  		[ring.middleware.params :refer :all]
  		[webapp.ff_home :refer :all]
  		[webapp.html_helpers :refer :all]
  		[webapp.microblog_home :refer :all]
  		[compojure.core :refer :all]; :only (GET POSTdefroutes context)]
        [ring.adapter.jetty :only (run-jetty)]
        [hiccup.core :refer :all]
        [hiccup.form :refer :all]))

(def site-map 
	(html
		[:h3 "Doug's Website"]
		[:ul (linkfy "microblog" "Micro-Blog")]
		[:ul (linkfy "ff" "Fantasy Football")]
		[:ul "Full Blog"]
		[:ul "Poetry Reader"]
		))

(defroutes router*
	(GET "/" request site-map)
	(GET "/ff" request ff)
	(GET "/microblog" request (microblog))
	(GET "/microblog/post" request (mb_post))
    (POST "/microblog_post" {params :params} (mb_post_post (params :message)))
	(compojure.route/not-found "Link not found."))

(def router (compojure.handler/api router*))