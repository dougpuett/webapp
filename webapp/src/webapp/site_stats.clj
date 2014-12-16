(ns webapp.site_stats
 (:refer-clojure :exclude [sort find])
 (:require
    [webapp.html_helpers :refer :all]
    [hiccup.core :refer :all]
    [cheshire.core :refer :all]))

(defn slurp-logs []	(map parse-string (clojure.string/split (slurp "resources/logs/logs.txt") #"\n")))
(defn stats-output [] (str site_header (html 
	[:body 
		[:h2 "Site Statistics"]
		[:div {:class "main"} 
			[:p {:style "padding-top:10px;padding-bottom:10px;padding-left:15px"} (str (html [:b "Total Page Views"] "--" (count (slurp-logs))))]]])))