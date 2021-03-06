(ns specter-reference.ch1.select-pred
  (:use com.rpl.specter com.rpl.specter.macros)
  (:use midje.sweet commons.clojure.core))


(facts "reference behavior for predicates"
  (fact "predicate success: the value is passed through to the result vector"
    (select [odd?] 1) => [1])

  (fact "as is common, predicates don't have to be strictly true or false"
    (select [#(get % :a)] {:a 1}) => [{:a 1}])

  (fact "predicate failure:, there is no result vector"
    ;; Note that this is different from the behavior of keywords:
    ;; there, a missing key results in `nil` being passed through
    (select [even?] 1) => nil)

  (fact "given multiple predicates, each passes judgment in turn"
    (select [integer? odd?] 1) => [1]
    (select [integer? even?] 1) => nil
    ;; Note that the first predicate's failure prevents the second
    ;; from being checked. (It would throw an exception if it were.)
    (select [integer? odd?] "hi") => nil)

  (fact "the result is specifically a vector"
    (select [odd?] 1) => vector?))
