(ns webapp.core
  (:refer-clojure :exclude [sort find])
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern])
  (:use [compojure.core :only (GET defroutes context)]
        [hiccup.core]
        [ring.adapter.jetty :only (run-jetty)])
  (:require (compojure handler route)[ring.util.response :as response]
        [monger.core :as mg]
        [monger.operators :refer :all]
        [monger.collection :as mc]
        [monger.conversion :refer [from-db-object]]
        [monger.query :refer :all]))

(defroutes router*
	(GET "/" (output "")))

(def router (compojure.handler/api router*))
