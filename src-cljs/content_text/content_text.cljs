(ns webapp.context_text
	(:import [goog.net XhrIo])
	(:require 
		[strokes :refer [d3]]
		[cognitect.transit :as t]))

(strokes/bootstrap)

(def r (t/reader :json))

(defn get-data [url cb]
	(XhrIo.send (str url)
		(fn [e]
			(let [xhr (.-target e)]
				(cb xhr)))))

(defn draw_text [text_data] (-> d3
	(.select "div#scroll")
	(.selectAll "p")
	(.data text_data)
	(.enter)
	(.append "p")
	(.text #(get % "text" "missing text"))))

(def content "/content_raw/beowulf_raw")
(defn get-data-text [] (get-data content #(draw_text (t/read r (.getResponseText %)))))

(get-data-text)