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

;; some of the ids:
#_(map :_id (posts))


(def object_ids {
  "54e2bf443004e55bd1b57656" ["tag" "tag2"] 
  "54e2bf3c3004e55bd1b57655" ["wamp"]
  "54e2bef13004e55bd1b57654" ["taggg"]
  "54e2bee33004e55bd1b57653" ["taggs"]
  "54e2be0a3004e55bd1b57652" ["tag" "taggity" "tag"]
  "54e2bdf43004e55bd1b57651" ["tag" "tag tag tag"]
  "54e2ba4030047809194ffa2e" ["tag"]
  "54dfbb043004bea05cfe9cc1" ["tagity" "tagiop!"]} )


(defn tagged_posts [] (mapv #(assoc % :tags (get object_ids (str (:_id %)))) (posts)))


;; get posts (json format)
(defn json_posts [] (generate-string ["posts" (into [] (map #(hash-map 
  "id" (str (:_id %))
  "message" (:message %) 
  "tags" (:tags %)
  "time" (parse_time (str (get % :_id)))) (tagged_posts)))])) 

;; write post and then returns json
(defn mb_post_return [message] (do (let [
    conn (mg/connect)
    db  (mg/get-db conn "webapp")
    coll "micro"]
  (mc/insert db coll {:message (html-sanitize policy message)}))
  (json_posts)))