(ns webapp.logging
	(:require [cheshire.core :refer :all]
		[clj-time.coerce :only '(to-long)]
		[clj-time.core :only '(now)]))

;; logging strategy?
;; persistent, text-based flat files for use with hive/pig. json format, date-partitioned. 
;; Clojure stats reading flat files to begin with?, should replace once we run into latency issues.


(defn log [request pagekey action ip-address in_map] 
	(spit "resources/logs/logs.txt" 
		(str (generate-string 
			(assoc in_map
				:timestamp (clj-time.coerce/to-long (clj-time.core/now))
				:pagekey pagekey
				:action action
				:ip-address ip-address
				:request-header (:headers request)
				)) "\n") :append true))