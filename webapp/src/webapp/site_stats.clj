(ns webapp.site_stats
 (:refer-clojure :exclude [sort find])
 (:require
    [webapp.html_helpers :refer :all]
    [hiccup.core :refer :all]
    [cheshire.core :refer :all]))

(defn slurp-logs []	(map #(parse-string % true) (clojure.string/split (slurp "resources/logs/logs.txt") #"\n")))
(defn pagekey_counts [] (frequencies (into [] (map :pagekey (filter #(= "page-view" (:action %)) (slurp-logs))))))

(defn stats-output [] (str site_header (html 
	[:body 
		[:h2 "Site Statistics"]
		[:div {:class "main"} 
			[:p {:style "padding-top:10px;padding-bottom:10px;padding-left:15px"} (str (html [:b "Total Page Views"] "--" (reduce + (map val (dissoc (pagekey_counts) "microblog_post")))))]
			[:p {:style "padding-left:15px"} (str "Homepage--" (get (pagekey_counts) "homepage"))]
			[:p {:style "padding-left:15px"} (str "Microblog--" (get (pagekey_counts) "microblog"))]
			[:p {:style "padding-left:15px"} (str "Fantasy Football--" (get (pagekey_counts) "fantasy-football"))]
			[:p {:style "padding-left:15px;padding-bottom:15px"} (str "Site Stats--" (get (pagekey_counts) "site_stats"))]]
	])))