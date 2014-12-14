(ns webapp.html_post
 (:refer-clojure :exclude [sort find])
 (:require
    [webapp.html_helpers :refer :all]
    [hiccup.core :refer :all]))

(def microblog_header (html [:head [:meta {:charset "utf-8"}]]))

(defn html_post [post] 
	(html 
		[:div {:style "background-color:white;width:500px;padding-top:5px;padding-bottom:5px;margin-bottom:4px;margin-left:50px;border-radius:6px"}
		[:div {:style "margin-left:10px;margin-right:5px"} 
			[:b "Doug Puett "]
			[:span {:style "color:grey"} (parse_time (str (:_id post)))]]
		[:div {:style "height:5px"}]
		[:div {:style "margin-left:10px;margin-right:5px"} (:message post)]
		]))

(defn microblog_html [posts] 
	(str 
		microblog_header 
		(html [:body {:style "background-color:#99CCFF;background: linear-gradient(to right, #99CCFF, #8AB8E6, #99CCFF);"} 
			[:div {:style "background-color:#99CCFF;height:0px"}]
			[:h2 {:style "margin-left:50px;padding-left:15px;background-color:white;width:485px;padding-top:14px;padding-bottom:14px;border-radius:6px"} "Doug's Microblog"]
			(map html_post (posts))
			])))