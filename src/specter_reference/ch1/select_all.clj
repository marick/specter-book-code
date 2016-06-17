(ns specter-reference.ch1.select-all
  ;; Specter is broken into two namespaces, one for macros, because of (1) ClojureScript and
  ;; (2) code that provides even better performance. In real life, you will probably want
  ;; to combine the two namespaces into a "catchall" namespace, using either the
  ;; [Potempkin](https://github.com/ztellman/potemkin) package or a facade over it like
  ;; [SuchWow](http://marick.github.io/suchwow/such.immigration.html)
  (:use com.rpl.specter com.rpl.specter.macros)
  (:use midje.sweet commons.clojure.core))

(fact "all by itself is a no-op"
  (select [ALL] [1 2 3 4]) => [1 2 3 4]
  (fact "except that ALL blows up given a non-collection"
    (select [ALL] 1) => (throws)))

(fact "Since ALL 'spreads' the elements, it can be used to flatten"
  (select [ALL ALL] [ [1] [2 3] ])
  =>                [  1   2 3  ]

  (fact "... but it won't flatten deeper than the level of nesting"
    (select [ALL ALL] [[0] [[1 2] 3]])
    =>                [ 0   [1 2] 3])

  (fact "both nil and an empty vector are flattened into nothing"
    (select [ALL ALL] [[1] nil [] [2]])
    =>                [ 1          2]))


(fact "ALL applies the rest of the path to each element"
  (select [ALL :a] [{:a 1} {:a 2} {   }])
  =>                   [1      2   nil ]
  (select [ALL even?] [1 2 3 4])
  =>                  [  2   4]
  (select [ALL :a even?] [{:a 1} {:a 2}])
  =>                     [           2 ]
  (select [ALL :a even?] [{:a 1}])
  =>                     [      ])

(fact "ALL can appear later in the vector"
  (select [:a ALL even?] {:a [1 2 3]})
  =>                         [  2  ])

(fact "The result is specifically a vector"
  (select [ALL] [1 2 3 4]) => vector?
  (select [ALL ALL] [[0] [[1 2] 3]]) => vector?
  (select [ALL even?] [1 2 3 4]) => vector?
  (select [ALL :a] [{:a 1} {:a 2} {   }]) => vector?)
