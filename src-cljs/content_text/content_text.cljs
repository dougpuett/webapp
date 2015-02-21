(ns webapp.context_text
	(:require-macros 
		[cljs.core.async.macros :refer [go]])
	(:require 
		[goog.dom :as dom]
		[goog.events :as events]
		[cognitect.transit :as t]
		[cljs.core.async :refer [<! put! chan]])
	(:import [goog.net XhrIo]
		[goog.net Jsonp]
		[goog Uri]))

;; CORE.ASYNC

;; the listen function takes an element and a response type, and then creates a listener that fires put! when the element and type are hit.
;; I don't know what the "e" is or where it comes from.
;; A parked channel is return with value "e", which may be the name of the element (?)
(defn listen [el type]
  (let [out (chan)]
	(events/listen el type
	  (fn [e] (put! out e)))
	out))

(defn my_xhr [uri]
	(let [out (chan)]
		(XhrIo.send uri (fn [res] (put! out (.getResponseJson (.-target res)))))
		out))


;; the my_xhr_post function takes a url and a message, creates a channel called "out", then puts the response of the post into that channel and returns it.
(defn my_xhr_post [uri message]
	(let [out (chan)]
		(XhrIo.send uri (fn [res] (put! out (.getResponseJson (.-target res)))) "POST" (str "message=" message))
			out))


(defn simple_xhr_post [uri message]
	(XhrIo.send uri (fn [res] (.getResponseJson (.-target res))) "POST" (str "message=" message)))

; (str "message=this is my message")
; (str "{\"message\":\"" message "\"}")
; (clj->js {"message" "this is my message"})

;; BEOWULF/THE WANDERER

(defn render-beowulf [results]
  (str
	"<div>"
	(apply str
	  (for [result (js->clj results)]
		(str "<p>" (get result "text") "</p>")))
	"</div>"))

(defn init_beowulf []
  (let [clicks (listen (dom/getElement "beowulf") "click")
		results-view (dom/getElement "scroll_results")
		content-title (dom/getElement "content_title")]
	(go (while true
		  (<! clicks)
		  (let [[_ results] (<! (my_xhr "/content_raw/beowulf_raw"))]
		  	(set! (.-innerHTML content-title) "Beowulf")
			(set! (.-innerHTML results-view) (render-beowulf results)))))))

(defn init_wanderer []
  (let [clicks (listen (dom/getElement "wanderer") "click")
		results-view (dom/getElement "scroll_results")
		content-title (dom/getElement "content_title")]
	(go (while true
		  (<! clicks)
		  (let [[_ results] (<! (my_xhr "/content_raw/wanderer_raw"))]
		  	(set! (.-innerHTML content-title) "The Wanderer")
			(set! (.-innerHTML results-view) (render-beowulf results)))))))

(defn init_clear []
  (let [clicks (listen (dom/getElement "clear") "click")
		results-view (dom/getElement "scroll_results")
		content-title (dom/getElement "content_title")]
	(go (while true
		  (<! clicks)
		  (let [[_ results] (<! (my_xhr "/content_raw/wanderer_raw"))]
		  	(set! (.-innerHTML content-title) "Content")
			(set! (.-innerHTML results-view) ""))))))

(init_wanderer)
(init_beowulf)
(init_clear)

;; POSTS
(defn str_tags [tags] (apply str (map #(str "<div class=\"microblog-tag\">" % "</div>") tags)))

(defn render-posts [results] (str
	"<div>"
	(apply str
		(for [result (js->clj results)]
			(apply str 
			"<div class=\"main\" style=\"margin-left:0\"><div style=\"margin-left:10px;margin-right:5px\"><div style=\"float:left;margin-right:2px\"><b>Doug Puett</b></div><div class=\"time\">"
			(get result "time")
			"</div>"
			(str_tags (get result "tags"))
			"</div><br />
			<div style=\"height:10px\"></div>
			<div style=\"margin-left:10px;margin-right:5px\">"
			(get result "message")
			"</div></div>")))
		"</div>"))

(def textarea_div (dom/getElement "post_text"))

(defn user-message []
  (.-value textarea_div))

(def textarea "<textarea cols=\"25\" form=\"microblog\" name=\"message\" placeholder=\"Note Goes here.\" rows=\"5\"></textarea>")

(defn init_poster []
  (let [clicks (listen (dom/getElement "post_button") "click")
		results-view (dom/getElement "scroll_posts")]
	(go (while true
		  (<! clicks) ;; this parks the while loop until we get a click
		  (let [[_ results] (<! (my_xhr_post "/microblog_post_return" (user-message)))
		  		results-view (dom/getElement "scroll_posts")]
				  	(set! (.-innerHTML results-view) (render-posts results)))))))

(init_poster)