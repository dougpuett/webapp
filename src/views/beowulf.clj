(ns views.beowulf
    (:require
        [cheshire.core :refer :all]
        [webapp.time :refer :all]
        [data.posts :refer :all]
        [views.html_post :refer :all]
        [views.html_helpers :refer :all]
        [hiccup.core :refer :all]))

(defn html_post_content [post] 
    (html 
        [:div {:class "main" :style "margin-left:0;padding-top:5px;padding-bottom:5px;margin-bottom:4px;"}
        [:div {:style "margin-left:10px;margin-right:0px"} 
            [:b "Doug Puett "]
            [:span {:style "color:grey"} (parse_time (str (:_id post)))]
            #_[:span {:style "color:blue"} "wamp"]]
        [:div {:style "height:5px"}]
        [:div {:style "margin-left:10px;margin-right:5px"} (:message post)]
        ]))

(defn mb_post_content [] (str site_header (html 
    [:form {:action "/microblog_post_content" :method "POST" :id "microblog"}]
    [:div {:id "text_area"}
    [:textarea {:name "message" :form "microblog" :placeholder "Note Goes here." :cols "25" :rows "5"}]
    [:div {:style "height:5px"}]
    [:button {:type "submit" :value "Submit" :form "microblog"} "Submit"]
    [:div {:style "height:10px"}]])))

(defn mb_post_content_2 [] (html 
    #_[:form {:action "/microblog_post_content" :method "POST" :id "microblog"}]
    [:div 
    [:textarea {:id "post_text" :name "message" :form "microblog" :placeholder "Note Goes here." :cols "25" :rows "5"}]
    [:div {:style "height:5px"}]
    [:button {:id "post_button" :type "submit" :value "Submit" :form "microblog"} "Submit"]
    [:div {:style "height:10px"}]]))

(defn content [posts] (str 
    site_header 
    (html
        [:body
        [:h2 {:id "content_title"} "Content"]
        [:div {:style "overflow:hidden;width:100%"}
            [:div {:id "scroll_main" :class "main" :style "height:500px;overflow:scroll;float:left"}
            [:p {:id "scroll_results"}]]
            [:div {:style "height:100px;margin-left:600px;padding:0px"} 
                [:div (mb_post_content_2)]
                [:div {:id "scroll_posts" :style "height:400px;overflow:scroll;padding:0px"}
                    (map html_post_left (second (parse-string (json_posts))))]]]
        [:div {:class "internal" :style "height:5px"}]
            [:button {:id "beowulf" :style "margin-left:50px"} "Beowulf!"]
            [:button {:id "wanderer" :style "margin-left:50px"} "The Wanderer!"]
            [:button {:id "clear" :style "margin-left:50px"} "Clear Text"]
            [:script {:type "text/javascript" :src "/goog/base.js"}]
            [:script {:type "text/javascript" :src "/js/content_text.js"}]])))