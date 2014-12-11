(ns webapp.html_helpers
	(:require [hiccup.core :refer :all]))

(defn linkfy [link text] (html [:a {:href link} text]))