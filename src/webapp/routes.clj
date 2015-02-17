(ns webapp.routes
	(:refer-clojure :exclude [find])
	(:require (compojure handler route core response)
		[cheshire.core :refer :all]
		[cognitect.transit :as transit]
		[compojure.core :refer :all]
		[compojure.response :refer :all]
		[compojure.handler :refer :all]
		[ring.adapter.jetty :only (run-jetty)]
		[ring.util.response :as response]
		[ring.middleware.params :refer :all]
		[taoensso.carmine :as car :refer (wcar)]
		[data.posts :refer :all]
		[data.ff_data :refer :all]
		[views.beowulf :refer :all]
		[webapp.controllers :refer :all]
		[webapp.password_admin :refer :all]
		[cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])))

;; =================
;; FILE DESCRIPTION:
;; =================

;; THIS FILE MAPS ROUTES TO FUNCTIONS. EACH ROUTE CALLS A FUNCTION THAT EITHER RETURNS HTML OR RAW DATA IN JSON FORM. IT ALSO CONTAINS A RING WRAPPER THAT TAKES CARE OF AUTHENTICATION BY CALLING A REDIS SERVER.

(defroutes router*
	;; ==========
	;; SITE INDEX
	;; ==========

	(GET "/" [] homepage_ctrl)
	(GET "/login" [] login-form)
	(GET "/logout" request (friend/logout* (response/redirect (str (:context request) "/"))))
	(GET "/site_stats" [] site_stats_ctrl)
	(GET "/webinar" [] webinar)
	;; =====
	;; BLOGS
	;; =====

	(GET "/my_posts" [] post_ctrl) ;; this is data. this is good.

	(GET "/microblog" [] microblog_ctrl) ;; decorated post data
	(GET "/microblog/post" [] microblog_post_ctrl) ;; decorated post box
	(POST "/microblog_post" [] microblog_publish_ctrl) ;; returns html page
	(POST "/microblog_post_return" [] microblog_publish_return_ctrl) ;; returns json

	(GET "/blog" [] blog_ctrl)
	(GET "/blog_index" [] blog_index_ctrl)
	(GET "/blog_posts_raw/:uri" req file_text)
	(GET "/blog_posts_raw/:year/:month/:day/:title" req 
		(file_text (assoc-in req [:params :uri] 
			(let 
				[params (:params req) 
				year (:year params)
				month (:month params)
				day (:day params)
				title (:title params)] (str year "/" month "/" day "/" title)))))

	(GET "/blog_posts/:uri" req file_text_decorated)
	(GET "/blog_posts/:year/:month/:day/:title" req 
		(file_text_decorated (assoc-in req [:params :uri] 
			(let 
				[params (:params req) 
				year (:year params)
				month (:month params)
				day (:day params)
				title (:title params)] (str year "/" month "/" day "/" title)))))

	;; ================
	;; FANTASY FOOTBALL
	;; ================

	(GET "/ff" [] fantasy-football_ctrl)
	;(GET "/ff_graph" [] d3_test)
	;(GET "/ff_data" _ (transit_write full_input))
	;(GET "/ff_data2" _ (transit_write output3))

	;; =======
	;; CONTENT
	;; =======

	(GET "/content" req (friend/authenticated (content_ctrl req)))
	(GET "/content_raw/:text" req content_data_ctrl)

	;; ====================
	;; AUTHENTICATION TESTS
	;; ====================

	; (GET "/role-user" req
	;	(friend/authorize #{:webapp.core/user :webapp.core/admin} "You're a user!"))
	; (GET "/role-admin" req
	; 	(friend/authorize #{:webapp.core/admin} "You're an admin!"))
	; (GET "/request/:test" req (str req))
	
	;; ================
	;; COMPOJURE EXTRAS
	;; ================
	
	(compojure.route/resources "/")
	(compojure.route/not-found "Link not found."))

;; ===============
;; AUTHENTICATION:
;; ===============

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