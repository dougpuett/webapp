(ns views.html_post
 (:refer-clojure :exclude [sort find])
 (:require
 	[webapp.time :refer :all]
 	[garden.core :refer [css]]
 	[garden.color :as color :refer [hsl rgb]]
 	[views.colors :refer :all]
    [views.html_helpers :refer :all]
    [hiccup.core :refer :all]))

;; =====================
;; HTML FOR READER PAGE:
;; =====================

(defn str_tags [tags] (html (map #(vector :div {:class "microblog-tag"} %) tags)))

(defn html_post_just [post left-text] 
	(html 
		[:div {:class "main" :style left-text}
		[:div {:style "margin-left:10px;margin-right:5px"} 
			[:div {:style "float:left;margin-right:2px"} [:b "Doug Puett"]]
			[:div {:class "time"} (parse_time (get post "id"))]
			(str_tags (get post "tags"))]
		[:br]
		[:div {:style "height:10px"}]
		[:div {:style "margin-left:10px;margin-right:5px"} (get post "message")]
		]))

(defn html_post [post] (html_post_just post ""))
(defn html_post_left [post] (html_post_just post "margin-left:0px"))

(defn microblog_html [posts] 
	(str 
		site_header 
		(html [:body
			[:h2  "Doug's Microblog"]
			[:div {:class "internal" :style "height:5px;"}]
			(map html_post posts)
			])))

;; ===================
;; HTML FOR POST PAGE:
;; ===================

(defn mb_post [] (str site_header (html 
	[:body
	[:h2 "Doug's Microblog"]
	[:div {:class "main" :style "margin-left:50px;padding-left:15px;padding-top:15px"} [:b "Message:"]
	[:form {:action "/microblog_post" :method "POST" :id "microblog"}]
	[:div 
	[:textarea {:name "message" :form "microblog" :placeholder "Message Goes here." :cols "25" :rows "5"}]
	[:div {:style "height:5px"}]
	[:button {:type "submit" :value "Submit" :form "microblog"} "Submit"]
	[:div {:style "height:10px"}]]]])))

;; =====================
;; HTML FOR POST SUBMIT:
;; =====================

(def message_submitted (str site_header (html 
	[:body 
	[:h2  "Doug's Microblog"]
	[:div {:class "main" :style "padding-left:15px;padding-top:14px;padding-bottom:14px"} [:b "Message Submitted!!!!"]
	[:form {:action "/microblog/post" :method "GET" :id "post"}]
	[:form {:action "/microblog" :method "GET" :id "view"}]
	[:div {:style "height:5px"}]
	[:div [:button {:type "submit" :value "Submit" :form "post"} "Post Again"]
	[:span [:button {:type "submit" :value "Submit" :form "view"} "View Post"]]]]])))