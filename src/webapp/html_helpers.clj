(ns webapp.html_helpers
	(:refer-clojure :exclude [second extend])
	(:require [hiccup.core :refer :all]
			[clj-time.coerce :refer :all]
			[webapp.color_palette :refer :all]
			[clj-time.format :refer :all]
			[clj-time.local :as l]
		 	[garden.core :refer [css]]))

(defn linkfy [link text] (html [:a {:href link} text]))

(def today-formatter (formatter "HH:mm, MM/dd"))
(def legacy-formatter (formatter "MM/dd/yyyy"))

(defn parse_time [mongo_time] (let [timestamp (* 1000 (Integer/parseInt (apply str (take 8 mongo_time)) 16))]
	(unparse 
	(if 
		(> (- (clj-time.coerce/to-long (clj-time.core/now)) 86400000) timestamp)
		legacy-formatter
		today-formatter)
	 (l/to-local-date-time (clj-time.coerce/from-long timestamp)))))

(def background-style "linear-gradient(to right, #99CCFF, #7AA3CC, #99CCFF)")
(def site_style (css 
	[:body {:background background-style :margin-top "10px" :margin-left "10px"}]
	[:.main {:border-radius "6px" :background-color "white" :margin-left "50px" :width "500px"}]
	[:svg {:border-radius "6px" :background-color "white" :margin-left "50px" :width "1000px" :height "500px" :padding "20px"}]
	[:h2 {:border-radius "6px" :padding "10px" :margin-left "50px" :width "485px" :background-color "white"}]
	[:.internal {:background background-style}]
	[:input {:border-radius "5px" :border "1px solid black"}]
	[:a:link {:color "#4C6680"}]
	[:a:visited {:color "#4C6680"}]
	[:textarea {:outline "none" :resize "none" :font-size "16px"}]
	[:.axis [:path :line {:fill "none" :stroke "black" :shape-rendering "crispEdges"}]]))

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