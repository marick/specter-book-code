(ns fun.reference.ch1.select-pred
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))


(facts "reference behavior for predicates"
  (fact "predicate success: the value is passed through to the result vector"
    (s/select [odd?] 1) => [1])

  (fact "predicate failure:, there is no result vector"
    ;; Note that this is different from the behavior of keywords:
    ;; there, a missing key results in `nil` being passed through
    (s/select [even?] 1) => nil)

  (fact "given multiple predicates, each passes judgment in turn"
    (s/select [integer? odd?] 1) => [1]
    (s/select [integer? even?] 1) => nil
    (s/select [integer? odd?] "hi") => nil))

