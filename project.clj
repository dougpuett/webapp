(defproject webapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [compojure "1.2.0"]
                 [ring "1.3.1"]
                 [com.novemberain/monger "2.0.0"]
                 [cheshire "5.3.1"]
                 [clj-time "0.8.0"]
                 [garden "1.2.5"]
                 [org.clojure/clojurescript "0.0-2498"]
                 [net.drib/strokes "0.5.1"]
                 [com.taoensso/carmine "2.9.0"]
                 [com.cemerick/friend "0.2.1"]
                 [com.cognitect/transit-clj "0.8.229"]
                 [com.cognitect/transit-cljs "0.8.194"]
                 [autoclave "0.1.7" :exclusions 
                    [com.google.guava/guava com.google.code.findbugs/jsr305 com.google.code.findbugs/jsr305]]]
  :plugins [[lein-ring "0.8.11"]
            [lein-cljsbuild "1.0.4-SNAPSHOT"]]
  :main ^:skip-aot webapp.core
;  :target-path "target/%s"
  :ring {:handler webapp.core/router}
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths 
          ; ["src-cljs/ff_season.cljs"] ;; "../target/classes"] ?? see http://swannodette.github.io/2014/12/22/waitin/
          ["src-cljs/ff_season"]
        :cache-analysis true
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          ; :output-to "target/cljsbuild-main.js"
          :output-to "resources/public/js/ff_season.js"
          :optimizations :whitespace
          :pretty-print false}}]}
;  :profiles {:uberjar {:aot :all}}
)