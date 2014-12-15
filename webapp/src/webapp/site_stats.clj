(ns webapp.site_stats
 (:refer-clojure :exclude [sort find])
 (:require
    [webapp.html_helpers :refer :all]
    [hiccup.core :refer :all]
    [cheshire.core :refer :all]))

(defn slurp-logs []	(map parse-string (clojure.string/split (slurp "resources/logs/logs.txt") #"\n")))
(defn stats-output [] (html [:h2 "Site Statistics"][:p (str "Total Page Views--" (count (slurp-logs)))]))