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
                 [autoclave "0.1.7" :exclusions [com.google.guava/guava com.google.code.findbugs/jsr305 com.google.code.findbugs/jsr305]]]
  :plugins [[lein-ring "0.8.11"]]
  :main ^:skip-aot webapp.core
;  :target-path "target/%s"
  :ring {:handler webapp.core/router}
;  :profiles {:uberjar {:aot :all}}
)