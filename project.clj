(defproject webapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [compojure "1.2.0"]
                 [org.clojure/clojure "1.6.0"]
                 [ring "1.3.1"]
                 [com.novemberain/monger "2.0.0"]
                 [cheshire "5.3.1"]
                 [sablono "0.2.20"]
                 [com.andrewmcveigh/cljs-time "0.3.2"]
                 [clj-time "0.8.0"]
                 [garden "1.2.5"]
                 [markdown-clj "0.9.62"]
                 [org.clojure/clojurescript "0.0-2913"]
                 [net.drib/strokes "0.5.1"]
                 [com.taoensso/carmine "2.9.0"]
                 [com.cemerick/friend "0.2.1"]
                 [com.cognitect/transit-clj "0.8.229"]
                 [com.cognitect/transit-cljs "0.8.194"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [autoclave "0.1.7" :exclusions 
                    [com.google.guava/guava com.google.code.findbugs/jsr305 com.google.code.findbugs/jsr305]]]
  :node-dependencies [[source-map-support "0.2.8"]]                    
  :plugins [[lein-ring "0.8.11"]
            [lein-npm "0.4.0"]
            [lein-cljsbuild "1.0.4-SNAPSHOT"]]
  :main ^:skip-aot webapp.routes
;  :target-path "target/%s"
  :ring {:handler webapp.routes/router}
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
    :builds [#_{
        ; The path to the top-level ClojureScript source directory:
        :source-paths 
          ; ["src-cljs/ff_season.cljs"] ;; "../target/classes"] ?? see http://swannodette.github.io/2014/12/22/waitin/
          ["src-cljs/ff_season"]
        :cache-analysis true
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          :id "ff"
          ; :output-to "target/cljsbuild-main.js"
          :output-to "resources/public/js/ff_season.js"
          :optimizations :whitespace
          :pretty-print false}}
        {:source-paths ["src-cljs/content_text"]
         :id "content"
         :cache-analysis true
         :compiler {
          :output-to "resources/public/js/content_text.js"
          :optimizations :advanced
          :pretty-print false}}
        {:source-paths ["src-cljs/webinar"]
         :id "content"
         :cache-analysis true
         :compiler {
          :output-to "resources/public/js/webinar.js"
          :optimizations :whitespace
          :pretty-print false}}                    
        ]}
;  :profiles {:uberjar {:aot :all}}
)