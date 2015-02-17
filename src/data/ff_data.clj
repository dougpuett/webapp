(ns data.ff_data
    (:import (java.io ByteArrayOutputStream))
    (:require
        [cheshire.core :refer :all]
        [clojure.java.io :as io]))

(def ff_input (mapv parse-string (clojure.string/split (slurp (io/file (io/resource "public/data/team_data.json"))) #"\n")))

; (def output2 (into [] (flatten (for [[team weeks] (group-by #(get % "team") ff_input)] 
;     (let [sorted-weeks (sort-by #(get % "week") weeks)]
;         (map #(hash-map 
;             "team" team 
;             "x1" (get %1 "week") 
;             "y1" (get %1 "odds") 
;             "x2" (get %2 "week") 
;             "y2" (get %2 "odds")) 
;         sorted-weeks (rest sorted-weeks)))))))

; (def output3 (vec (concat output2 (take 25 output2))))

; (defn write [x]
;   (let [baos (ByteArrayOutputStream.)
;         w    (transit/writer baos :json)
;         _    (transit/write w x)
;         ret  (.toString baos)]
;     (.reset baos)
;     ret))

; (defn transit_write [output]
;     {:header {"Content-Type" "application/transit+json"}
;     :body (write output)})