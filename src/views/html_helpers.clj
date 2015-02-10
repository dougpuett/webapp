(ns views.html_helpers
	(:refer-clojure :exclude [second extend])
	(:require [hiccup.core :refer :all]
			[clj-time.coerce :refer :all]
			[views.colors :refer :all]
			[clj-time.format :refer :all]
			[clj-time.local :as l]
			[garden.units :as u :refer [px pt]]
			[garden.color :as color :refer [hsl rgb]]
		 	[garden.core :refer [css]]))

(defn linkify [link text] (html [:a {:href link} text]))

;; =========================================================================================
;; MOVE TO API
;; =========================================================================================
(def today-formatter (formatter "HH:mm, MM/dd"))
(def legacy-formatter (formatter "MM/dd/yyyy"))

(defn parse_time [mongo_time] (let [timestamp (* 1000 (Integer/parseInt (apply str (take 8 mongo_time)) 16))]
	(unparse 
	(if 
		(> (- (clj-time.coerce/to-long (clj-time.core/now)) 86400000) timestamp)
		legacy-formatter
		today-formatter)
	 (l/to-local-date-time (clj-time.coerce/from-long timestamp)))))
;; =========================================================================================


#_(def background-style "linear-gradient(to right, #99CCFF, #7AA3CC, #99CCFF)")
#_(def background-style (str "linear-gradient(to right," (get theme 2) "," (get theme 1) "," (get theme 2) ")"))
(def site_style (css
	[:body {
		:background-color (:background theme) 
		:margin-top "10px" 
		:margin-left "10px"
		:font-family "Georgia"
		:color (:text theme)}]
	[:.main {
		:border-radius "6px" 
		:background-color (:field theme)
		:margin-left "50px" 
		:width "500px"}]
	[:svg {
		:border-radius "6px" 
		:background-color "white" 
		:margin-left "50px" 
		:width "1000px" 
		:height "500px" 
		:padding "20px"}]
	[:h2,:h3 {
		:border-radius "6px" 
		:padding "10px" 
		:margin-left "50px" 
		:width "485px" 
		:background-color (:background theme)
		:color (:text-accent theme)}]
	[:h3 {
		:padding (px 0)
		:padding-left (px 10)
		:margin-left (px 60)
		:margin-top (px -20)
		:font-size (px 16)
		}]
	[:.blog-text {
		:border-radius "6px" 
		:background-color (:field theme)
		:margin-left "50px" 
		:width "500px"
		}]
	[:p {
		:padding (px 10)
		}]
	[:.internal {
		:background-color (:background theme)}]
	[:input {
		:border-radius "5px" 
		:border "1px solid black"}
		:background-color (:text theme)]
	[:.tag {
		:background-color "#2E4372" 
		:border-radius "5px" 
		:border "1px solid black"}]
	[:a:link {
		:color (:text theme) }];;"#4C6680"}]
	[:a:visited {
		:color (:text theme) }];;"#4C6680"}]
	[:textarea {
		:outline "none" 
		:resize "none" 
		:font-size "16px"}]
	[:.axis [:path :line {
		:fill "none" 
		:stroke "black" 
		:shape-rendering "crispEdges"}]]))

(def graph_objects (css
	(let [this_color (get red 1)] (vector :.graph_object_1 {:stroke this_color :fill this_color}))
	(let [this_color (get orange 2)] (vector :.graph_object_2 {:stroke this_color :fill this_color}))
	(let [this_color (get yellowest 3)] (vector :.graph_object_3 {:stroke this_color :fill this_color}))
	(let [this_color (get green 2)] (vector :.graph_object_4 {:stroke this_color :fill this_color}))
	(let [this_color (get teal 1)] (vector :.graph_object_5 {:stroke this_color :fill this_color}))
	(let [this_color (get blue 2)] (vector :.graph_object_6 {:stroke this_color :fill this_color}))
	(let [this_color (get indigo 1)] (vector :.graph_object_7 {:stroke this_color :fill this_color}))
	(let [this_color (get purple-again 3)] (vector :.graph_object_8 {:stroke this_color :fill this_color}))
	(let [this_color (get pink 2)] (vector :.graph_object_9 {:stroke this_color :fill this_color}))
	(let [this_color (get red 3)] (vector :.graph_object_10 {:stroke this_color :fill this_color}))
	(let [this_color (get green 4)] (vector :.graph_object_11 {:stroke this_color :fill this_color}))
	(let [this_color (get blue 3)] (vector :.graph_object_12 {:stroke this_color :fill this_color}))))

(def site_header (html [:head [:meta {:charset "utf-8"}][:style site_style]]))
(defn html_wrap [title content] (html [:head [:meta {:charset "utf-8"}][:title title][:style site_style]][:body content]))
