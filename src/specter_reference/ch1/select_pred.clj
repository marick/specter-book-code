(ns specter-reference.ch1.select-pred
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))


(facts "reference behavior for predicates"
  (fact "predicate success: the value is passed through to the result vector"
    (s/select [odd?] 1) => [1])

  (fact "as is common, predicates don't have to be strictly true or false"
    (s/select [#(get % :a)] {:a 1}) => [{:a 1}])

  (fact "predicate failure:, there is no result vector"
    ;; Note that this is different from the behavior of keywords:
    ;; there, a missing key results in `nil` being passed through
    (s/select [even?] 1) => nil)

  (fact "given multiple predicates, each passes judgment in turn"
    (s/select [integer? odd?] 1) => [1]
    (s/select [integer? even?] 1) => nil
    ;; Note that the first predicate's failure prevents the second
    ;; from being checked. (It would throw an exception if it were.)
    (s/select [integer? odd?] "hi") => nil)

  (fact "the result is specifically a vector"
    (s/select [odd?] 1) => vector?))
