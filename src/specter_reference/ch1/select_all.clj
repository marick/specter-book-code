(ns specter-reference.ch1.select-all
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))

(fact "ALL applies the rest of the selector to each element"
  (s/select [s/ALL even?] [1 2 3 4])
  =>                      [  2   4]
  (s/select [s/ALL :a even?] [{:a 1} {:a 2}])
  =>                         [           2 ]
  (s/select [s/ALL :a even?] [{:a 1}])
  =>                         [      ]

  (fact "exception when a predicate blows up"
    (s/select [s/ALL :a even?] [{:a 1} {:b 1}]) => (throws)
    ;; workaround
    (s/select [s/ALL :a integer? even?] [{:a 1} {:b 88} {:a 200}]) => [200]))

(fact "Since ALL 'spreads' the elements, it can be used to flatten vectors"
  (s/select [s/ALL s/ALL] [ [1] [] [2 3] ])
  =>                      [  1      2 3  ]

  (fact "but it won't flatten deeper than the level of vector nesting"
    (s/select [s/ALL s/ALL] [ [1] [:next-too-deep [2 3]] ])
    =>                      [  1   :next-too-deep [2 3]  ])

  (fact "both `nil` and `[]` can be flattened out of existence"
    (s/select [s/ALL s/ALL] [ [1] [2] [] nil])
    =>                      [  1   2        ])
  )

(fact "ALL can appear later in the vector"
  (s/select [:a s/ALL even?] {:a [1 2 3]})
  =>                             [  2  ])

(fact "ALL blows up given a non-collection"
  (s/select [s/ALL] 1) => (throws))
