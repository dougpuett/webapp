(ns webapp.html_helpers
	(:refer-clojure :exclude [second extend])
	(:require [hiccup.core :refer :all])
	(:require [clj-time.coerce :refer :all])
	(:require [clj-time.format :refer :all])
	(:require [clj-time.local :as l])	)

(defn linkfy [link text] (html [:a {:href link} text]))

(def custom-formatter (formatter "yyyy-MM-dd HH:mm:ss"))
(defn parse_time [mongo_time] (unparse custom-formatter (l/to-local-date-time (clj-time.coerce/from-long (* 1000 (Integer/parseInt (apply str (take 8 mongo_time)) 16))))))