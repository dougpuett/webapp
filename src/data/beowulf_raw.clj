(ns data.beowulf_raw
  (:require
  	[clojure.java.io :as io]))

(def beowulf_raw_array (clojure.string/split (slurp (io/file (io/resource "texts/beowulf.txt"))) #"\n"))
(def wanderer_raw_array (clojure.string/split (slurp (io/file (io/resource "texts/wanderer.txt"))) #"\n"))

(def beowulf_raw (mapv #(hash-map "text" %1 "line_no" %2) beowulf_raw_array (range 1 100000)))

(def wanderer_raw (mapv #(hash-map "text" %1 "line_no" %2) wanderer_raw_array (range 1 100000)))