(ns data.blog_posts
 (:require
    [markdown.core :refer :all]
    [clojure.java.io :as io]
    [cheshire.core :refer :all]))

;; =================
;; HELPER FUNCTIONS:
;; =================

(defn destructure-title [title] {:date (take 10 title) :title (clojure.string/replace (apply str (drop 11 title)) #"_" " ")})

(defn read-resources [in-string] (.list (io/file (io/resource in-string))))

;; READ NESTED DIRECTORIES:
;; years
(def all-years-raw (read-resources "blog/"))
(def all-years (filter 
    #(and 
        (= 4 (count %)) 
        (< 2014 (bigint %))
        (> 2020 (bigint %))) 
    all-years-raw))

;; months
(def all-months-filter
    #(and 
        (= 2 (count %)) 
        (< 0 (bigint %))
        (> 13 (bigint %))))

(def all-months-raw (reduce 
    into [] 
    (map 
        #(map (fn [x] (vector % x)) (read-resources (str "blog/" %)))
            all-years)))

(def all-months (filter #(all-months-filter (second %)) all-months-raw))

;; days
(def all-days-filter
    #(and 
        (= 2 (count %)) 
        (< 0 (bigint %))
        (> 32 (bigint %))))

(def all-days-raw (reduce 
    into [] 
    (map 
        #(map (fn [x] (conj % x)) (read-resources (str "blog/" (clojure.string/join #"/" %))))
            all-months)))

(def all-days (filter #(all-days-filter (get % 2)) all-days-raw))

;; posts
(def all-posts-filter
    #(and 
        (not= \. (first %))))

(def all-posts-raw (reduce 
    into [] 
    (map 
        #(map (fn [x] (conj % x)) (read-resources (str "blog/" (clojure.string/join #"/" %))))
            all-days)))

(def all-posts (filter #(all-posts-filter (get % 3)) all-posts-raw))

(def blog-all-posts (reverse (sort (map #(clojure.string/join #"/" %) all-posts))))



;; ============
;; OUTPUT APIS:
;; ============
;; (def blog-all-posts (.list (io/file (io/resource "blog/"))))

(defn blog-text [file] (slurp (io/file (io/resource (str "blog/" file)))))

(defn blog-map [file] (let [title-map (destructure-title file)]
    (assoc title-map :text (md-to-html-string (slurp (io/file (io/resource (str "blog/" file))))))))

(defn blog-json [file] (generate-string blog-map))