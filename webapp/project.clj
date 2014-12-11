(defproject webapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [compojure "1.1.9"]
                 [ring "1.3.1"]
                 [com.novemberain/monger "2.0.0"]
]
  :plugins [[lein-ring "0.8.11"]]
;  :main ^:skip-aot webapp.core
;  :target-path "target/%s"
  :ring {:handler webapp.core/router}
;  :profiles {:uberjar {:aot :all}}
)
