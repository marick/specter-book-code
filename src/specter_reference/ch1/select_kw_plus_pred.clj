(ns specter-reference.ch1.select-kw-plus-pred
  (:use com.rpl.specter com.rpl.specter.macros)
  (:use midje.sweet commons.clojure.core))

(facts "combining keywords and predicates"
  (select [:a map? :b] {:a 1}) => nil
  (select [:a map? :b] {:a {:b 1}}) => [1]
  (select [:a map? :b] {:a {}}) => [nil]

  (select [map? :a] {:b 1}) => [nil]
  (select [map? :a] 1) => nil)
