(ns webapp.beowulf
    (:require
        [webapp.beowulf_raw :refer :all]
        [webapp.microblog_home :refer :all]
        [webapp.html_helpers :refer :all]
        [hiccup.core :refer :all]))

(def beowulf [:div {:class "main" :style "height:500px;overflow:scroll;float:left"} (map #(vector :p %) beowulf_raw)])

(defn html_post_content [post] 
    (html 
        [:div {:class "main" :style "margin-left:0;padding-top:5px;padding-bottom:5px;margin-bottom:4px;"}
        [:div {:style "margin-left:10px;margin-right:0px"} 
            [:b "Doug Puett "]
            [:span {:style "color:grey"} (parse_time (str (:_id post)))]]
        [:div {:style "height:5px"}]
        [:div {:style "margin-left:10px;margin-right:5px"} (:message post)]
        ]))

(defn mb_post_content [] (str site_header (html 
    [:form {:action "/microblog_post_content" :method "POST" :id "microblog"}]
    [:div 
    [:textarea {:name "message" :form "microblog" :placeholder "Note Goes here." :cols "25" :rows "5"}]
    [:div {:style "height:5px"}]
    [:button {:type "submit" :value "Submit" :form "microblog"} "Submit"]
    [:div {:style "height:10px"}]])))

(defn content [] (str 
    site_header 
    (html 
        [:h2 "Beowulf"]
        [:div {:style "overflow:hidden;width:100%"}
            beowulf
            #_[:div {:class "internal" :style "height:10px;float:left"}] 
            [:div {:style "height:100px;margin-left:600px;padding:0px"} 
                [:div (mb_post_content)]
                [:div {:style "height:400px;overflow:scroll;padding:0px"}
                    (map html_post_content (posts))]]])))