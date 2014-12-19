(ns webapp.logging
	(:require [cheshire.core :refer :all]
		[taoensso.carmine :as car :refer (wcar)]
		[clj-time.coerce :only '(to-long)]
		[clj-time.format :refer :all]
		[clj-time.core :only '(now)]))

;; logging strategy?
;; persistent, text-based flat files for use with hive/pig. json format, date-partitioned. 
;; Clojure stats reading flat files to begin with?, should replace once we run into latency issues.

(def server1-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))
(def date-formatter (formatter "yyyy:MM:dd"))

(defn log [request pagekey action ip-address in_map]
	(let [now  (clj-time.core/now)]
	(do 
		(spit "resources/logs/logs.txt" 
			(str (generate-string 
				(assoc in_map
				:timestamp (clj-time.coerce/to-long now)
				:pagekey pagekey
				:action action
				:ip-address ip-address
				:request-header (:headers request)
				)) "\n") :append true)
		;; redis log counts:
		(wcar* (car/incr (str "log:" action ":" pagekey ":" (unparse date-formatter now))))
		(wcar* (car/incr (str "log:" action ":" pagekey)))
		(wcar* (car/incr (str "log:" action ":" (unparse date-formatter now))))
		(wcar* (car/incr (str "log:" action))))))

;; ============
;; LOG READERS:
;; ============

; (defn slurp-logs []	(map #(parse-string % true) (clojure.string/split (slurp "resources/logs/logs.txt") #"\n")))
; (defn pagekey_counts [] (frequencies (into [] (map :pagekey (filter #(= "page-view" (:action %)) (slurp-logs))))))
