(ns webapp.controllers
    (:refer-clojure :exclude [sort find])
    (:require
        [apis.beowulf_raw :refer :all]
        [apis.ff_data :refer :all]
        [apis.blog_posts :refer :all]
        [apis.posts :refer :all]

        [views.html_helpers :refer :all]
        [views.html_post :refer :all]
        [views.beowulf :refer :all]

        [webapp.logging :refer :all]
        [webapp.site_stats :refer :all]
        [webapp.ff_home :refer :all]

        [cheshire.core :refer :all]
        [garden.core :refer [css]]
        [cemerick.friend :as friend] (cemerick.friend [workflows :as workflows] [credentials :as creds])
        [hiccup.core :refer :all]
        [hiccup.element :refer :all]))

;; =================
;; FILE DESCRIPTION:
;; =================

;; THIS FILE CONTAINS ALL OF THE FUNCTIONS THAT SERVER CONTENT TO THE WEB SERVER. IDEALLY, THERE SHOULDN'T BE ANY HTML ANYWHERE HERE, BECAUSE ALL HTML SHOULD COME FROM THE VIEWS DIRECTORY (LET'S RENAME THIS WHEN WE HAVE TIME). THERE SHOULD ALSO NOT BE ANY DATA CALLS, SINCE THOSE SHOULD ALL BE IN THE API DIRECTORY. 

;; THIS FILE CONTAINS TWO DUTIES:

;;    1. PARSE REQUEST CALLS

;;    2. COMBINE DATA AND HTML WRAPPERS

;; PROGRESSION SHALL BE MADE BY PUSHING LOGICAL SYSTEMS AND DESIGN SYSTEMS APART AS WELL AS SEGMENTING THIS FILE INTO SEPERATE PRODUCT LINES THAT ARE AS DISCRETE AS POSSIBLE.

;; ===================
;; SITE ADMINISTRATION
;; ===================

(def site-map 
    (str site_header (html
    [:body 
    [:h2 "Doug's Website"]
    [:div {:class "main" :style "padding-bottom:5px"}
    [:p {:style "padding:15px;padding-bottom:0px"} [:b "Pages:"]]
    [:ul (linkify "blog" "Blog")]
    [:ul (linkify "microblog" "Micro-Blog")]
    #_[:ul (linkify "ff" "Fantasy Football")]
    #_[:ul (linkify "ff_graph" "FF Graph (D3.js Example)")]
    [:ul (linkify "content" "Content App")]
    [:ul "FF Graph"]
    #_[:ul "Full Blog"]
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

(defn site_stats_ctrl [request] (do 
    (log request "site_stats" "page-view" (:remote-addr request) {}) 
    (stats-output)))


;; =========
;; MICROBLOG
;; =========

(defn microblog_ctrl [request] (do 
    (log request "microblog" "page-view" (:remote-addr request) {}) 
    (microblog)))

(defn microblog_post_ctrl [request] (friend/authenticated (do 
    (log request "microblog_post" "page-view" (:remote-addr request) {}) 
    (mb_post))))

(defn microblog_publish_ctrl [request] (let [message (:message (:params request))] (do 
    (log request "microblog_post" "publish" (:remote-addr request) {:message message}) 
    (mb_post_return message))))

(defn post_ctrl [request] (friend/authenticated (json_posts)))

;; =========
;; FULL BLOG
;; =========
;; HELPERS
(defn prettify-title [title-map] (apply str (:title title-map) ", " (:date title-map)))

(def blog_index (html_wrap "Index" (html [:h2 "Index"] [:div {:class "main"} (ordered-list 
    (map 
        linkify 
        (map 
            #(str "blog_posts/" %) 
            blog-all-posts) 
        (map 
            #(prettify-title (destructure-title %))
            blog-all-posts)))])))

(defn decorate-blog-map [blog-map] (html [:h2 (:title blog-map)][:h3 (:date blog-map)][:div {:class "blog-text" :style "width:1000px"} (:text blog-map)]))

(defn blog_data [_] (html_wrap "Doug's Blog" (map #(decorate-blog-map (blog-map %)) blog-all-posts)))

;; CONTROLLERS
(defn blog_index_ctrl [_] blog_index)

(defn file_text [request] (let [uri (get (:params request) :uri)] (blog-text uri)))

(defn file_text_decorated [request] (let
    [uri (get (:params request) :uri)
    blog-map (blog-map uri)]
    (html_wrap (:title blog-map) (decorate-blog-map blog-map))))

(defn blog_ctrl [request] (do 
    (log request "blog" "page-view" (:remote-addr request) {}) 
    blog_data))

;; =======
;; CONTENT
;; =======

(def beowulf_json (generate-string ["text" beowulf_raw]))
(def wanderer_json (generate-string ["text" wanderer_raw]))
(def content_dict {"beowulf_raw" beowulf_json "wanderer_raw" wanderer_json})

(defn content_ctrl [request] (do
    (log request "content reader" "page-view" (:remote-addr request) {})
    (content)))

(defn content_data_ctrl [request] (get content_dict (get (:params request) :text)))

;; ==========
;; D3 TEST/FF
;; ==========

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

(defn fantasy-football_ctrl [request] (do 
    (log request "fantasy_football" "page-view" (:remote-addr request) {})
    ff))