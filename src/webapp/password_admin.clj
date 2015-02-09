(ns webapp.password_admin
  (:refer-clojure :exclude [sort find])
  (:import org.mindrot.jbcrypt.BCrypt)
  (:require
      [taoensso.carmine :as car :refer (wcar)]
      [cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])))

(def server1-conn {:pool {} :spec {:host "127.0.0.1" :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn set_roles_to_redis [username password roles] (wcar* (car/set (str "password:" username) {:hashed-password (creds/hash-bcrypt password) :roles roles})))

; (set_roles_to_redis "" "" #{:webapp.core/user})

(defn get_roles_from_redis [username] (wcar* (car/get (str "password:" username))))

(defn redis-credentials
	"This will be a function that takes a map {:username X :password Y} and returns {:username X :roles Z} iff hash(Y) = Y' (hashed password in redis)."
		[{:keys [username password]}]
		(let [{:keys [roles hashed-password]} (get_roles_from_redis username)]
			(if 
				(nil? hashed-password)
				nil
					(if 
						(BCrypt/checkpw password hashed-password)
						{:username username :roles roles}
						nil))))