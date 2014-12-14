(ns webapp.microblog_home
 (:refer-clojure :exclude [sort find])
 (:import [com.mongodb MongoOptions ServerAddress]
      [org.bson.types ObjectId]
      [com.mongodb DB WriteConcern])
 (:require
    [monger.core :as mg]
    [webapp.html_helpers :refer :all]
    [monger.operators :refer :all]
    [monger.collection :as mc]
    [webapp.html_post :refer :all]
    [monger.conversion :refer [from-db-object]]
    [monger.query :refer :all]))

(defn posts [] (map #(into {} %) (let [
   conn (mg/connect)
   db  (mg/get-db conn "webapp")
   coll "micro"]
 (with-collection db coll 
 	(find {})
 	(sort {:_id -1})))))

(defn microblog [] (microblog_html posts))

(defn mb_post_post [message] (do (let [
		conn (mg/connect)
		db  (mg/get-db conn "webapp")
		coll "micro"]
	(mc/insert db coll {:message message}))
	(str "Message Submitted!!!")))