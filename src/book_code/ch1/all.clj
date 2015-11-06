(ns book-code.ch1.all
  (:use midje.sweet commons.clojure.core)
  (:use book-code.ch1.realistic))

(deftype AllType [])
(def ALL (->AllType))

(extend-type AllType
  StructurePath
  (select* [this structure next-fn]
    (into [] (mapcat next-fn structure))))



(facts "about ALL"
  (fact "all by itself is a no-op"
    (select [ALL] [1 2 3 4]) => [1 2 3 4])

  (fact "Since ALL 'spreads' the elements, it can be used to flatten"
    (select [ALL ALL] [ [1] [2 3] ])
    =>                [  1   2 3  ]

    (fact "... but it won't flatten deeper than the level of nesting"
      (select [ALL ALL] [[0] [[1 2] 3]])
      =>                [ 0   [1 2] 3])

    (fact "both nil and an empty vector are flattened into nothing"
      (select [ALL ALL] [[1] nil [] [2]])
      =>                [ 1          2]))


  (fact "ALL applies the rest of the selector to each element"
    (select [ALL :a] [{:a 1} {:a 2} {   }])
    =>               [1      2   nil ]
    (select [ALL even?] [1 2 3 4])
      =>                      [  2   4]
      (select [ALL :a even?] [{:a 1} {:a 2}])
      =>                     [           2 ])

  (fact "ALL returns vectors"
    (select [ALL] '(1 2 3)) => vector?
    (select [ALL even?] [1 2 3]) => vector?))



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

(facts "all forms return specifically vectors"
  (select [:a :b] {:a {:b 1}}) => vector?
  (select [odd?] 1) => vector?)
