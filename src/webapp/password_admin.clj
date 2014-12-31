(ns webapp.password_admin
  (:refer-clojure :exclude [sort find])
  (:require
      [taoensso.carmine :as car :refer (wcar)]
      [cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])))

(def server1-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn set_roles_to_redis [username password roles] (wcar* (car/set (str "password:" username) {:hashed-password (creds/hash-bcrypt password) :roles roles})))

(set_roles_to_redis "" "" #{:webapp.core/user})
(set_roles_to_redis "doug_admin" "pimpin" #{:webapp.core/admin})