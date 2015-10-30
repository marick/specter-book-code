(defproject fun "0.0.1"
  :description "Source code for /Extending and Using Specter/"
  :url "https://leanpub.com/specter"
  :pedantic? :warn
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/mit-license.php"}


  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.rpl/specter "0.8.0" :exclusions [org.clojure/clojure org.clojure/clojurescript]]
                 [marick/suchwow "4.4.0" :exclusions [org.clojure/clojure org.clojure/clojurescript]]
                 [marick/clojure-commons "1.1.1" :exclusions [org.clojure/clojure]]
]
  :profiles {:dev {:dependencies [[midje "1.8.1" :exclusions [org.clojure/clojure]]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0-beta1"]]}
             }
  :plugins [[lein-midje "3.2"]]

  :aliases {"compatibility" ["with-profile" "+1.6+1.7:+1.8" "midje" ":config" ".compatibility-test-config"]
            "travis" ["with-profile" "+1.6:+1.7:+1.8" "midje"]}

  ;; For Clojure snapshots
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  )
