(defproject om-comment-box "0.1.0-SNAPSHOT"
  :description "My first Om program!"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/core.async "0.2.371"]
                 [org.omcljs/om "1.0.0-alpha24"]]
  :plugins [[lein-figwheel "0.5.0-2"]]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/"]
                        :figwheel true
                        :compiler {:main "om-comment-box.core"
                                   :asset-path "js"
                                   :output-to "resources/public/js/main.js"
                                   :output-dir "resources/public/js"}}]})
