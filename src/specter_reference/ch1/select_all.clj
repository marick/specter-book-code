(ns specter-reference.ch1.select-all
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))


(fact "all by itself is a no-op"
  (s/select [s/ALL] [1 2 3 4]) => [1 2 3 4]
  (fact "except that ALL blows up given a non-collection"
    (s/select [s/ALL] 1) => (throws)))

(fact "Since ALL 'spreads' the elements, it can be used to flatten"
  (s/select [s/ALL s/ALL] [ [1] [2 3] ])
  =>                      [  1   2 3  ]

  (fact "... but it won't flatten deeper than the level of nesting"
    (s/select [s/ALL s/ALL] [[0] [[1 2] 3]])
    =>                      [ 0   [1 2] 3])

  (fact "both nil and an empty vector are flattened into nothing"
    (s/select [s/ALL s/ALL] [[1] nil [] [2]])
    =>                      [ 1          2]))


(fact "ALL applies the rest of the selector to each element"
  (s/select [s/ALL :a] [{:a 1} {:a 2} {   }])
  =>                       [1      2   nil ]
  (s/select [s/ALL even?] [1 2 3 4])
  =>                      [  2   4]
  (s/select [s/ALL :a even?] [{:a 1} {:a 2}])
  =>                         [           2 ]
  (s/select [s/ALL :a even?] [{:a 1}])
  =>                         [      ])


(fact "ALL can appear later in the vector"
  (s/select [:a s/ALL even?] {:a [1 2 3]})
  =>                             [  2  ])

