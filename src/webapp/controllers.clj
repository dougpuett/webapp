(ns webapp.controllers
    (:refer-clojure :exclude [sort find])
    (:require
        [webapp.ff_home :refer :all]
        [webapp.html_helpers :refer :all]
        [webapp.html_post :refer :all]
        [webapp.microblog_home :refer :all]
        [webapp.logging :refer :all]
        [webapp.site_stats :refer :all]
    [cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])
    [hiccup.core :refer :all]))

(def site-map 
    (str site_header (html
    [:body 
    [:h2 "Doug's Website"]
    [:div {:class "main" :style "padding-bottom:5px"}
    [:p {:style "padding:15px;padding-bottom:0px"} [:b "Pages:"]]
    [:ul (linkfy "microblog" "Micro-Blog")]
    [:ul (linkfy "ff" "Fantasy Football")]
    [:ul "Full Blog"]
    [:ul "Poetry Reader"]
    [:ul (linkfy "site_stats" "Site Statistics")]
    ]])))

(def login-form (html
    [:div {:class "row"}
    [:div {:class "columns small-12"}
    [:h3 "Login"]
    [:div {:class "row"}
    [:form {:method "POST" :action "login" :class "columns small-4"}
    [:div "Username" [:input {:type "text" :name "username"}]]
    [:div "Password" [:input {:type "password" :name "password"}]]
    [:div [:input {:type "submit" :class "button" :value "Login"}]]]]]]))

(defn homepage_ctrl [request] (do 
    (log request "homepage" "page-view" (:remote-addr request) {}) 
    site-map))

(defn fantasy-football_ctrl [request] (do 
    (log request "fantasy_football" "page-view" (:remote-addr request) {})
    ff))

(defn microblog_ctrl [request] (do 
    (log request "microblog" "page-view" (:remote-addr request) {}) 
    (microblog)))

(defn microblog_post_ctrl [request] (friend/authenticated (do 
    (log request "microblog_post" "page-view" (:remote-addr request) {}) 
    (mb_post))))

(defn microblog_publish_ctrl [request] (let [message (:message (:params request))] (do 
    (log request "microblog_post" "publish" (:remote-addr request) {:message message}) 
    (mb_post_post message))))

(defn site_stats_ctrl [request] (do 
    (log request "site_stats" "page-view" (:remote-addr request) {}) 
    (stats-output)))