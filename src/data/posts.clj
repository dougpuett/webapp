(ns data.posts
 (:refer-clojure :exclude [sort find])
 (:import [com.mongodb MongoOptions ServerAddress]
      [org.bson.types ObjectId]
      [com.mongodb DB WriteConcern])
 (:require
    [autoclave.core :refer :all]
    [cheshire.core :refer :all]
    [clojure.java.io :as io]
    [hiccup.core :refer :all]  

    [webapp.time :refer :all]

    [monger.core :as mg]
    [monger.operators :refer :all]
    [monger.collection :as mc]
    [monger.conversion :refer [from-db-object]]
    [monger.query :refer :all]))

;; functions needed: microblog, mb_post, mb_post_return, json_posts

;; ===========================
;; LOGIC FOR MICROBLOG READER:
;; ===========================

;; get posts from db
(defn posts [] (map #(into {} %) (let [
   conn (mg/connect)
   db  (mg/get-db conn "webapp")
   coll "micro"]
 (with-collection db coll 
 	(find {})
 	(sort {:_id -1})))))

;; TO DO:

;; get tags from db
;; process tag data
;; merge tag data with raw post tags

;; HTML policy... (TO DO): move to markdown

(def policy (html-policy :allow-elements ["a" "em" "b"]
                         :allow-attributes ["href" :on-elements ["a"]]
                         :allow-standard-url-protocols
                         :require-rel-nofollow-on-links))

;; ===========================
;; MICROBLOG API:
;; ===========================

;; get posts (json format)
(defn json_posts [] (generate-string ["posts" (into [] (map #(hash-map "message" (get % :message) "time" (parse_time (str (get % :_id)))) (posts)))]))

;; write post and then returns json
(defn mb_post_return [message] (do (let [
    conn (mg/connect)
    db  (mg/get-db conn "webapp")
    coll "micro"]
  (mc/insert db coll {:message (html-sanitize policy message)}))
  (json_posts)))