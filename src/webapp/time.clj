(ns webapp.time
  (:require
    		[clj-time.coerce :refer :all]
			[clj-time.format :refer :all]
			[clj-time.local :as l]))


(def today-formatter (formatter "HH:mm, MM/dd"))
(def legacy-formatter (formatter "MM/dd/yyyy"))

(defn parse_time [mongo_time] (let [timestamp (* 1000 (Integer/parseInt (apply str (take 8 mongo_time)) 16))]
	(unparse 
	(if 
		(> (- (clj-time.coerce/to-long (clj-time.core/now)) 86400000) timestamp)
		legacy-formatter
		today-formatter)
	 (l/to-local-date-time (clj-time.coerce/from-long timestamp)))))