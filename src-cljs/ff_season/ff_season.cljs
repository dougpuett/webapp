(ns webapp.d3test
	(:import [goog.net XhrIo])
	(:require 
		[strokes :refer [d3]]
		[cognitect.transit :as t]))

(strokes/bootstrap)

(def height 460)
(def width 960)

(def team_to_obj {
	"Pat Sajak Off" "graph_object_1"
	"RGIII's iCloud Pics" "graph_object_2"
	"What's Fappening" "graph_object_3"
	"Champ's Smelly Meat" "graph_object_4"
	"The Manifest Destinies" "graph_object_5"
	"Nebraska Cornholers" "graph_object_6"
	"Team buckley" "graph_object_7"
	"Team Freedman" "graph_object_8"
	"Team Stanley" "graph_object_9"
	"Bill and Ted's ExcellentAdv" "graph_object_10"
	"I Didn't Know You Were A Cop" "graph_object_11"
	"Samuel Beckittns" "graph_object_12"
	})

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

(defn draw_circles [circle_data x_field y_field] (-> svg
	(.selectAll "circle")
	(.data circle_data)
	(.enter)
	(.append "circle")
	(.attr {
		:cx #(+ 30 (xScale (get % x_field))), 
		:cy #(+ 20 (yScale (get % y_field))), 
		:class #(get team_to_obj (get % "team")), 
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
		:class #(get team_to_obj (get % "team"))
		:stroke-width 3})))

(defn get-data [url cb]
	(XhrIo.send (str url)
		(fn [e]
			(let [xhr (.-target e)]
				(cb xhr)))))

(defn get-data-circles [] (get-data "/ff_data" #(draw_circles (t/read r (.getResponseText %)) "week" "odds")))
(defn get-data-segs [] (get-data "/ff_data2" #(draw_lines (t/read r (.getResponseText %)))))

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

(get-data-segs)
(get-data-circles)
