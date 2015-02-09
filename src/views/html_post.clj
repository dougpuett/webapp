(ns views.html_post
 (:refer-clojure :exclude [sort find])
 (:require
    [views.html_helpers :refer :all]
    [hiccup.core :refer :all]))

;; =====================
;; HTML FOR READER PAGE:
;; =====================

(defn html_post [post] 
	(html 
		[:div {:class "main" :style "padding-top:5px;padding-bottom:5px;margin-bottom:4px;"}
		[:div {:style "margin-left:10px;margin-right:5px"} 
			[:b "Doug Puett "]
			[:span {:style "color:grey"} (parse_time (str (:_id post)))]
				#_[:div {:class "tag"} "Who do we 'ave ere?"]]
		[:div {:style "height:5px"}]
		[:div {:style "margin-left:10px;margin-right:5px"} (:message post)]
		]))

(defn microblog_html [posts] 
	(str 
		site_header 
		(html [:body
			[:h2  "Doug's Microblog"]
			[:div {:class "internal" :style "height:5px;"}]
			(map html_post (posts))
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