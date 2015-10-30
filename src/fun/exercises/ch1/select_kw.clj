(ns fun.exercises.ch1.select-kw
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))


(facts "Mimic Specter's behavior with keywords"
  (fact "descends down a map"
    (s/select [:a] {:a 1}) => [1]
    (s/select [:a :b] {:a {:b 1}}) => [1])

  (fact "when a keyword is not present, behaves like Clojure and passes `nil` along"
    (s/select [:a] {}) => [nil]
    (s/select [:a] {:not-a 1}) => [nil]
    (s/select [:a :b] {:a {}}) => [nil])

  (fact "like `get` on keywords, even bogus values are silently accepted"
    (s/select [:a] nil) => [nil]
    (s/select [:a] :something-random) => [nil]
    (s/select [:a :b] {:a 1}) => [nil]))
