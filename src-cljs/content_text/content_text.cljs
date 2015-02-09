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

;; IMPLEMENT THIS: http://jsfiddle.net/W33YR/3/


;; CORE.ASYNC

(defn listen [el type]
  (let [out (chan)]
	(events/listen el type
	  (fn [e] (put! out e)))
	out))

(defn my_xhr [uri]
	(let [out (chan)]
		(XhrIo.send uri (fn [res] (put! out (.getResponseJson (.-target res)))))
		out))

(defn my_xhr_post [uri message]
	(let [out (chan)]
		(XhrIo.send uri (fn [res] (put! out (.getResponseJson (.-target res)))) "POST" (str "message=" message))
			out))

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

(defn render-posts [results] 
	(str
		"<div>"
		(apply str
			(for [result (js->clj results)]
				(str
				"<div class=\"main\" style=\"margin-left:0;padding-top:5px;padding-bottom:5px;margin-bottom:4px;\">
					<div style=\"margin-left:10px;margin-right:0px\">
						<b>Doug Puett </b>
						<span style=\"color:grey\">"
							(get result "time") 
						"</span>
					</div>
					<div style=\"height:5px\">
					</div>
					<div style=\"margin-left:10px;margin-right:5px\">" 
					(get result "message")
					"</div
				></div>")))
		"</div>"))

(defn user-message []
  (.-value (dom/getElement "post_text")))

(def textarea_div (dom/getElement "post_text"))

(def textarea "<textarea cols=\"25\" form=\"microblog\" name=\"message\" placeholder=\"Note Goes here.\" rows=\"5\"></textarea>")

(defn init_poster []
  (let [clicks (listen (dom/getElement "post_button") "click")
		results-view (dom/getElement "scroll_posts")]
	(go (while true
		  (<! clicks)
		  (let [[_ results]  (<! (my_xhr_post "/microblog_post" (user-message)))
		  		results-view (dom/getElement "scroll_posts")]
				  	(set! (.-innerHTML results-view) (render-posts results)))))))

(init_poster)