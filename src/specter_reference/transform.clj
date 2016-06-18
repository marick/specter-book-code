(ns specter-reference.transform
  ;; Specter is broken into two namespaces, one for macros, because of (1) ClojureScript and
  ;; (2) code that provides even better performance. In real life, you will probably want
  ;; to combine the two namespaces into a "catchall" namespace, using either the
  ;; [Potempkin](https://github.com/ztellman/potemkin) package or a facade over it like
  ;; [SuchWow](http://marick.github.io/suchwow/such.immigration.html)
  (:use com.rpl.specter com.rpl.specter.macros)
  (:use midje.sweet commons.clojure.core))

(fact "transform applies functions"
  ;; Notice that, unlike `select`, it does *not* wrap results in a vector"
  (transform [:a] inc {:a 1}) => {:a 2}
  (transform [odd?] inc 1) => 2
  (transform [odd?] inc 2) => 2
  (transform [ALL] inc [1 2 3]) => [2 3 4])

(fact "applies transform function only to path endpoints"
  (transform [:a :b] inc {:a {:b 1} :b "hi"}) => {:a {:b 2} :b "hi"}
  (transform [ALL :b] inc [{:b 1} {:b 2}]) => [{:b 2} {:b 3}]
  (transform [:b ALL] inc {:b [1 2 3]}) => {:b [2 3 4]}
  (transform [ALL even?] inc [1 2 3]) => [1 3 3]
  (transform [ALL identity] str [true false nil 1]) => ["true" false nil "1"]

  (transform [ALL :b] inc [1 {:b 1}]) => (throws)
  (transform [ALL map? :b] inc [1 {:b 1}]) => [1 {:b 2}])

(fact "odd cases"
  (transform [:a :b] inc {}) => (throws)
  (transform [:a :b] inc {:a {}}) => (throws)

  (transform [even?] inc "string") => (throws)
  (transform [ALL] inc nil) => nil)

(fact "predicates are happy with `nil` results"
  (transform [even?] (constantly nil) 2) => nil
  (transform [vector? ALL even?] (constantly nil) [2]) => [nil])

(fact "ALL produces a `seq`, not a `vector`"
  (let [result (transform [ALL] inc (list 1 2 3))]
    result => seq?
    result =not=> vector?))
