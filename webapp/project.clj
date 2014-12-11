(defproject webapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [hiccup "1.0.3"]
                 [compojure "1.1.5"]
                 [ring/ring-jetty-adapter "1.1.8"]
]
  :main ^:skip-aot webapp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
