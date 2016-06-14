(defproject fun "0.1.0"
  :description "Source code for /Extending and Using Specter/"
  :url "https://leanpub.com/specter"
  :pedantic? :warn
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/mit-license.php"}


  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.rpl/specter "0.11.2" :exclusions [org.clojure/clojure org.clojure/clojurescript]]
                 [marick/suchwow "5.1.3" :exclusions [org.clojure/clojure org.clojure/clojurescript]]
                 [marick/clojure-commons "2.0.1" :exclusions [org.clojure/clojure]]
]
  :profiles {:dev {:dependencies [[midje "1.9.0-alpha2" :exclusions [org.clojure/clojure]]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha5"]]}
             }
  :plugins [[lein-midje "3.2"]]

  :aliases {"compatibility" ["with-profile" "+1.6:+1.7:+1.8:+1.9" "midje" ":config" ".compatibility-test-config"]
            "travis" ["with-profile" "+1.6:+1.7:+1.8:+1.9" "midje"]}

  ;; For Clojure snapshots
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  )
