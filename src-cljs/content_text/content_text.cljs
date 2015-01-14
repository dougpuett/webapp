(ns webapp.context_text
	(:require-macros [cljs.core.async.macros :refer [go]])
	(:require 
		#_[strokes :refer [d3]]
		[goog.dom :as dom]
		[goog.events :as events]
		[cognitect.transit :as t]
		[cljs.core.async :refer [<! put! chan]])
	(:import [goog.net XhrIo]
		[goog.net Jsonp]
		[goog Uri]))

;; GOOGLE CLOSURE API

(defn get-data [url cb]
	(XhrIo.send (str url)
		(fn [e]
			(let [xhr (.-target e)]
				(cb xhr)))))

(defn post-data [url cb message]
	(XhrIo.send (str url)
		(fn [e]
			(let [xhr (.-target e)]
				(cb xhr)))
		"POST"))

;; CORE.ASYNC

(defn listen [el type]
  (let [out (chan)]
    (events/listen el type
      (fn [e] (put! out e)))
    out))


(defn render-query [results]
  (str
    "<ul>"
    (apply str
      (for [result (js->clj results)]
        (str "<li>" (get result "text") "</li>")))
    "</ul>"))

(defn my_xhr [uri]
	(let [out (chan)]
		(XhrIo.send uri (fn [res] (put! out (.getResponseJson (.-target res)))))
		out))

(defn init []
  (let [clicks (listen (dom/getElement "wanderer") "click")
        results-view (dom/getElement "scroll_results")]
    (go (while true
          (<! clicks)
          (let [[_ results] (<! (my_xhr "http://localhost:4000/content_raw/beowulf_raw"))]
            (set! (.-innerHTML results-view) (render-query results)))))))

(init)