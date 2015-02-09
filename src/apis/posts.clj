(ns apis.posts
 (:refer-clojure :exclude [sort find])
 (:import [com.mongodb MongoOptions ServerAddress]
      [org.bson.types ObjectId]
      [com.mongodb DB WriteConcern])
 (:require
    [clojure.java.io :as io]
    [monger.core :as mg]
    [hiccup.core :refer :all]
    [cheshire.core :refer :all]
    [autoclave.core :refer :all]
    [views.html_helpers :refer :all]
    [monger.operators :refer :all]
    [monger.collection :as mc]
    [views.html_post :refer :all]
    [monger.conversion :refer [from-db-object]]
    [monger.query :refer :all]))

;; ===========================
;; LOGIC FOR MICROBLOG READER:
;; ===========================

(defn posts [] (map #(into {} %) (let [
   conn (mg/connect)
   db  (mg/get-db conn "webapp")
   coll "micro"]
 (with-collection db coll 
 	(find {})
 	(sort {:_id -1})))))

(defn html_poster [post] 
  {:id (parse_time (str (:_id post)))
    :message (:message post)})


(defn microblog [] (microblog_html posts))

(defn json_posts [] (generate-string ["posts" (into [] (map #(hash-map "message" (get % :message) "time" (parse_time (str (get % :_id)))) (posts)))]))

;; ===========================
;; LOGIC FOR MICROBLOG POSTER:
;; ===========================
(def policy (html-policy :allow-elements ["a" "em" "b"]
                         :allow-attributes ["href" :on-elements ["a"]]
                         :allow-standard-url-protocols
                         :require-rel-nofollow-on-links))

(defn mb_post_post [message] (do (let [
		conn (mg/connect)
		db  (mg/get-db conn "webapp")
		coll "micro"]
	(mc/insert db coll {:message (html-sanitize policy message)}))
	message_submitted))

(defn mb_post_return [message] (do (let [
    conn (mg/connect)
    db  (mg/get-db conn "webapp")
    coll "micro"]
  (mc/insert db coll {:message (html-sanitize policy message)}))
  (json_posts)))