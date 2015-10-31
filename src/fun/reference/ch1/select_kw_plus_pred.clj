(ns fun.reference.ch1.select-kw-plus-pred
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))

(facts "combining keywords and predicates"
  (s/select [:a map? :b] {:a 1}) => nil
  (s/select [:a map? :b] {:a {:b 1}}) => [1]
  (s/select [:a map? :b] {:a {}}) => [nil]

  (s/select [map? :a] {:b 1}) => [nil]
  (s/select [map? :a] 1) => nil)
