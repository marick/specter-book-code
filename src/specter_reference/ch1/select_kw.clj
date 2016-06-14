(ns specter-reference.ch1.select-kw
  (:use com.rpl.specter com.rpl.specter.macros)
  (:use midje.sweet commons.clojure.core))

(facts "Specter's behavior with keywords"
  (fact "descends a map"
    (select [:a] {:a 1}) => [1]
    (select [:a :b] {:a {:b 1}}) => [1])

  (fact "missing keyword has a value of `nil`"
    (select [:a] {}) => [nil]
    (select [:a] {:not-a 1}) => [nil]
    (select [:a :b] {:a {}}) => [nil])

  (fact "as with `get`, bogus structures are silently accepted"
    (select [:a] nil) => [nil]               ; (get nil :a) => nil
    (select [:a] "no-map") => [nil]          ; (get "no-map" :a) => nil
    (select [:a :b] {:a 1}) => [nil])       ; (get 1 :a) => nil

  (fact "the result is specifically a vector"
    (select [:a :b] {:a {:b 1}}) => vector?))
