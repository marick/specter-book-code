(ns specter-reference.ch1.select-kw
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))


(facts "Specter's behavior with keywords"
  (fact "descends a map"
    (s/select [:a] {:a 1}) => [1]
    (s/select [:a :b] {:a {:b 1}}) => [1])

  (fact "missing keyword has a value of `nil`"
    (s/select [:a] {}) => [nil]
    (s/select [:a] {:not-a 1}) => [nil]
    (s/select [:a :b] {:a {}}) => [nil])

  (fact "as with `get`, bogus structures are silently accepted"
    (s/select [:a] nil) => [nil]               ; (get nil :a) => nil
    (s/select [:a] "no-map") => [nil]          ; (get "no-map" :a) => nil
    (s/select [:a :b] {:a 1}) => [nil]))       ; (get 1 :a) => nil
