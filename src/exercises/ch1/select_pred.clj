(ns exercises.ch1.select-pred
  (:use midje.sweet commons.clojure.core))

(defn select-pred [selector candidate]
  )

(future-fact "our implementation matches Specter's"
  (select-pred [odd?] 1) => [1]
  (select-pred [even?] 1) => nil
  (select-pred [integer? odd?] 1) => [1]
  (select-pred [integer? even?] 1) => nil
  (select-pred [integer? odd?] "hi") => nil)
