(ns webapp.site_stats
 (:refer-clojure :exclude [sort find second extend])
 (:require
    [webapp.html_helpers :refer :all]
    [taoensso.carmine :as car :refer (wcar)]
    [hiccup.core :refer :all]
    [webapp.logging :refer :all]
    [clj-time.core :as time]
    [clj-time.format :refer :all]
    [cheshire.core :refer :all]))

(defn to-date-offset [n] (time/plus (time/date-time 2014 12 01) (time/days n)))
(defn get_log [log_key] (wcar* (car/get log_key)))
(defn date_log [date-offset] (let [query (get_log (str "log:page-view:" (unparse date-formatter (to-date-offset date-offset))))] 
	(if (nil? query) 0 query)))

(defn stats-output [] (str site_header (html 
	[:body 
		[:h2 "Site Statistics"]
		[:div {:class "main"} 
			[:p {:style "padding-top:10px;padding-bottom:10px;padding-left:15px"} (str (html [:b "Total Page Views"] "--" (wcar* (car/get "log:page-view"))))]
			[:p {:style "padding-left:15px"} (str "Homepage--" (wcar* (car/get "log:page-view:homepage")))]
			[:p {:style "padding-left:15px"} (str "Microblog--" (wcar* (car/get "log:page-view:microblog")))]
			[:p {:style "padding-left:15px"} (str "Fantasy Football--" (wcar* (car/get "log:page-view:fantasy_football")))]
			[:p {:style "padding-left:15px;padding-bottom:15px"} (str "Site Stats--" (wcar* (car/get "log:page-view:site_stats")))]]
		;; BY DATE QUERY FOR FUTURE GRAPHS:
		; [:div {:class "main"} (clojure.string/join (map date_log (range 1 30)))]
	])))