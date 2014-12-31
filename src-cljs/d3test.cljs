(ns webapp.d3test
	(:import [goog.net XhrIo])
	(:require 
		[strokes :refer [d3]]
		[cognitect.transit :as t]))

(strokes/bootstrap)

(def height 460)
(def width 960)

(def svg (-> d3 
	(.select "body")
	(.append "svg")
	(.attr {:width width :height height})))

(def r (t/reader :json))

(def xScale (-> d3
	.-scale
	(.linear)
	(.domain [0 13])
	(.range [0 width])))

(def yScale (-> d3
	.-scale
	(.linear)
	(.domain [0 1])
	(.range [height 0])))

(defn draw_circles [circle_data x_field y_field class_name] (-> svg
	(.selectAll "circle")
	(.data circle_data)
	(.enter)
	(.append "circle")
	(.attr {
		:cx #(+ 30 (xScale (get % x_field))), 
		:cy #(+ 20 (yScale (get % y_field))), 
		:class class_name, 
		:r 5})
	(.append "title")
	(.text #(get % "team"))))

(defn draw_lines [line_data] (-> svg
	(.selectAll "line")
	(.data line_data)
	(.enter)
	(.append "line")
	(.attr {
		:x1 #(+ 30 (xScale (get % "x1")))
		:y1 #(+ 20 (yScale (get % "y1")))
		:x2 #(+ 30 (xScale (get % "x2")))
		:y2 #(+ 20 (yScale (get % "y2")))
		:stroke "steelblue"
		:stroke-width 3})))

(defn get-data []
	(XhrIo.send (str "/ff_data")
		(fn [e]
			(let [xhr (.-target e)]
				#_(t/read r (.getResponseText xhr))
				(draw_circles (t/read r (.getResponseText xhr)) "week" "odds" "right")))))

(defn get-data-seg []
	(XhrIo.send (str "/ff_data2")
		(fn [e]
			(let [xhr (.-target e)]
				#_(t/read r (.getResponseText xhr))
				(draw_lines (t/read r (.getResponseText xhr)))))))


(def x-axis (-> d3 .-svg (.axis)
	(.scale xScale)
	(.orient "bottom")))

(def y-axis (-> d3 .-svg (.axis)
	(.scale yScale)
	(.orient "left"))) 

(-> svg (.append "g")
	(.attr {:class "x axis"
		:transform (str "translate(30," (+ 20 height) ")")})
	(.call x-axis))

(-> svg (.append "g")
	(.attr {:class "y axis"
		:transform (str "translate(30," 20 ")")})
	(.call y-axis))

(get-data-seg)
(get-data)