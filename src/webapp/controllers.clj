(ns webapp.controllers
    (:refer-clojure :exclude [sort find])
    (:require
        [webapp.ff_home :refer :all]
        [cheshire.core :refer :all]
        [views.html_helpers :refer :all]
        [views.html_post :refer :all]
        [apis.posts :refer :all]
        [views.beowulf :refer :all]
        [webapp.logging :refer :all]
        [webapp.site_stats :refer :all]
        [garden.core :refer [css]]
    [cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])
    [hiccup.core :refer :all]))

(def d3_test (str site_header (html 
    [:style (str graph_objects (css 
        [:circle {:fill-opacity 0.5 :stroke-opacity 1 :stroke-width "2px"}]
        [:circle:hover {:fill-opacity 0.75}]
        #_[:svg:hover {:fill-opacity 0.15}]))]
    [:script {:type "text/javascript" :src "http://d3js.org/d3.v3.min.js"}]
    [:body 
        [:h2 "Fantasy Football Playoff Odds"]
        [:div 
        [:script {:type "text/javascript" :src "/js/ff_season.js"}]]])))

(def d3_base (str site_header (html [:div {:class "main"} ])))

(def site-map 
    (str site_header (html
    [:body 
    [:h2 "Doug's Website"]
    [:div {:class "main" :style "padding-bottom:5px"}
    [:p {:style "padding:15px;padding-bottom:0px"} [:b "Pages:"]]
    [:ul (linkify "microblog" "Micro-Blog")]
    [:ul (linkify "ff" "Fantasy Football")]
    [:ul (linkify "ff_graph" "FF Graph (D3.js Example)")]
    [:ul "Full Blog"]
    [:ul (linkify "content" "Poetry Reader")]
    [:ul (linkify "site_stats" "Site Statistics")]
    ]])))

(def login-form 
    (str site_header (html
    [:body
    [:div {:class ".internal"}
    [:h2 "Login"]
    [:div {:class "main"  :style "padding-top:10px"}
    [:form {:method "POST" :action "login" :class "columns small-4"}
    [:div {:style "padding-left:15px"} "Username"][:div {:style "padding-left:15px;padding-bottom:5px"}[:input {:type "text" :name "username"}]]
    [:div {:style "padding-left:15px"} "Password"][:div {:style "padding-left:15px;padding-bottom:5px"} [:input {:type "password" :name "password"}]]
    [:div {:style "padding-left:15px;padding-bottom:5px"}[:input {:type "submit" :class "button" :value "Login"}]]]]]])))

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
    (mb_post_return message))))

(defn site_stats_ctrl [request] (do 
    (log request "site_stats" "page-view" (:remote-addr request) {}) 
    (stats-output)))

(defn post_ctrl [request] (friend/authenticated (json_posts)))

