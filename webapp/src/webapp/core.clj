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

(defn linkfy [link text] (html [:a {:href link} text]))

(def site-map 
	(html
		[:h3 "Doug's Website"]
		[:ul (linkfy "microblog" "Micro-Blog")]
		[:ul (linkfy "ff" "Fantasy Football")]
		[:ul "Full Blog"]
		[:ul "Poetry Reader"]
		))

(def ff (html [:h2 "Championship Odds:"][:h3 "Before Week 1:"][:p "What's Fappening 42.2%, Team Freedman 29.3%, Bill and Ted's ExcellentAdv 15.5%, Champ's Smelly Meat 13.0%"][:h3 "After Week 1:"][:p "Team Freedman 49.4%, Champ's Smelly Meat 39.8%, Bill and Ted's ExcellentAdv 5.7%, What's Fappening 5.2%"][:h2 "Individual Matches:"][:h3 "Before Week 1:"][:p "Team Freedman 60.1%, Bill and Ted's ExcellentAdv 39.9%"][:p "What's Fappening 68.6%, Champ's Smelly Meat 31.4%"][:p "Samuel Beckittns 69.6%, Pat Sajak Off 30.4%"][:p "Team buckley 52.5%, I Didn't Know You Were A Cop 47.5%"][:p "Nebraska Cornholers 53.3%, RGIII's iCloud Pics 46.7%"][:p "The Manifest Destinies 82.0%, Team Stanley 18.0%"][:h3 "After Week 1:"][:p "Team Freedman 87.0%, Bill and Ted's ExcellentAdv 13.0%"][:p "Champ's Smelly Meat 90.2%, What's Fappening 9.8%"][:p "Samuel Beckittns 60.2%, Pat Sajak Off 39.8%"][:p "Team buckley 90.2%, I Didn't Know You Were A Cop 9.8%"][:p "Nebraska Cornholers 100.0%"][:p "The Manifest Destinies 95.3%, Team Stanley 4.7%"]))

(def posts (map #(get % "message") (let [conn (mg/connect)
      db   (mg/get-db conn "webapp")
      coll "micro"]
  (mc/find db coll {}))))

(def microblog (html (map #(vector :p %) posts)))

(defroutes router*
	(GET "/" request site-map)
	(GET "/ff" request ff)
	(GET "/microblog" request microblog)
	(compojure.route/not-found "Link not found."))

(def router (compojure.handler/api router*))