(ns fun.book.ch1.all
  (:use midje.sweet commons.clojure.core)
  (:use fun.book.ch1.realistic))

(deftype AllType [])

(extend-type AllType
  StructurePath
  (select* [this structure next-fn]
    (into [] (mapcat next-fn structure))))

(def ALL (->AllType))


(fact "ALL applies the rest of the selector to each element"
  (select [ALL even?] [1 2 3 4]) => [2 4]
  (select [ALL :a even?] [{:a 1} {:a 2}]) => [2]
  (select [ALL :a even?] [{:a 1}]) => []

  (select [ALL ALL] [ [1] [] [2 3] ]) => [1 2 3]
  (select [ALL ALL] [ [1] [:next-too-deep [2 3]] ]) => [  1   :next-too-deep [2 3]  ]
  (select [ALL ALL] [ [1] [2] [] nil]) => [1 2]

  (select [:a ALL even?] {:a [1 2 3]}) => [2])


;; Old tests continue to pass.

(fact "works the same for keywords"
  (select [:a] nil) => [nil]
  (select [:a] :something-random) => [nil]
  (select [:a] {:a 1}) => [1]
  (select [:a] {:not-a 1}) => [nil]
  (select [:a] {}) => [nil]

  (select [:a :b] {:a {:b 1}}) => [1]
  (select [:a :b] {:a 1}) => [nil]
  (select [:a :b] {:a {}}) => [nil])

(fact "works the same for predicates"
  (select [odd?] 1) => [1]
  (select [even?] 1) => nil
  (select [integer? odd?] 1) => [1]
  (select [integer? even?] 1) => nil
  (select [integer? odd?] "hi") => nil)

(facts "combining keywords and predicates"
  (select [:a map? :b] {:a 1}) => nil
  (select [:a map? :b] {:a {:b 1}}) => [1]
  (select [:a map? :b] {:a {}}) => [nil]
  (select [map? :a] {:b 1}) => [nil]
  (select [map? :a] 1) => nil)
