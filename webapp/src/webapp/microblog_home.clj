(ns webapp.microblog_home
 (:refer-clojure :exclude [sort find])
 (:import [com.mongodb MongoOptions ServerAddress]
      [org.bson.types ObjectId]
      [com.mongodb DB WriteConcern])
 (:require (compojure handler route)[ring.util.response :as response]
    [compojure.core :refer :all] ;:only (GET defroutes context)]
    [ring.adapter.jetty :only (run-jetty)]
    [ring.util.response :refer :all]
    [monger.core :as mg]
    [hiccup.core :refer :all]
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

(defn microblog_html [] (microblog posts))

(defn mb_post_post [message] (do (let [
		conn (mg/connect)
		db  (mg/get-db conn "webapp")
		coll "micro"]
	(mc/insert db coll {:message message}))
	(str "Message Submitted!!!")))

(defn mb_post [] (html 
	[:div [:h3 "Doug's Microblog Message Inputter"]]
	[:div "Message:"]
	[:form {:action "/microblog_post" :method "POST" :id "microblog"}]
	[:textarea {:name "message" :form "microblog"}]
	[:div [:button {:type "submit" :value "Submit" :form "microblog"} "Submit"]]))