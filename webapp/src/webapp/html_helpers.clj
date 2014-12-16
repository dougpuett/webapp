(ns webapp.html_helpers
	(:refer-clojure :exclude [second extend])
	(:require [hiccup.core :refer :all]
			[clj-time.coerce :refer :all]
			[clj-time.format :refer :all]
			[clj-time.local :as l]
		 	[garden.core :refer [css]]))

(defn linkfy [link text] (html [:a {:href link} text]))

(def custom-formatter (formatter "yyyy-MM-dd HH:mm:ss"))
(defn parse_time [mongo_time] (unparse custom-formatter (l/to-local-date-time (clj-time.coerce/from-long (* 1000 (Integer/parseInt (apply str (take 8 mongo_time)) 16))))))

(def site_style (css 
	[:body {:background "linear-gradient(to right, #99CCFF, #8AB8E6, #99CCFF)" :margin-top "10px" :margin-left "10px"}]
	[:.main {:border-radius "6px" :background-color "white" :margin-left "50px" :width "500px"}]
	[:h2 {:border-radius "6px" :padding "10px" :margin-left "50px" :width "485px" :background-color "white" }]
	[:.internal {:background "linear-gradient(to right, #99CCFF, #8AB8E6, #99CCFF)"}]))

(def site_header (html [:head [:meta {:charset "utf-8"}]][:style site_style]))
