(ns book-code.ch2.final
  (:use midje.sweet commons.clojure.core))

;;; Protocols

(defprotocol Navigator
  (select* [this structure continuation])
  (transform* [this structure continuation]))

;;; Generic support code

(defn navigation-worker [worker-kw path-element]
  (-> (find-protocol-impl Navigator path-element)
      (get worker-kw)))

(defn mkfn:worker-calling-continuation [worker-kw element continuation]
  (let [worker (navigation-worker worker-kw element)]
    (fn [structure]
      (worker element structure continuation))))

(defn predict-computation [worker-kw path final-action]
  (reduce (fn [continuation element]
            (mkfn:worker-calling-continuation worker-kw element continuation))
          final-action
          (reverse path)))

;;; Core functions

(defn select [path structure]
  ((predict-computation :select* path vector) structure))

(defn transform [path transform-fn structure]
  ((predict-computation :transform* path transform-fn) structure))

;;; Implementations of different types of path elements.

(extend-type clojure.lang.Keyword
  Navigator
  (select* [this structure continuation]
    (continuation (get structure this)))
  (transform* [this structure continuation]
    (->> (get structure this)
         continuation
         (assoc structure this))))

(extend-type clojure.lang.AFn
  Navigator
  (select* [this structure continuation]
    (if (this structure)
      (continuation structure)
      nil))
  (transform* [this structure continuation]
    (if (this structure)
      (continuation structure)
      structure)))

(deftype AllType [])
(def ALL (->AllType))

(extend-type AllType
  Navigator
  (select* [this structure continuation]
    (into [] (mapcat continuation structure)))
  (transform* [this structure continuation]
    ;; It happens that `ALL` produces a lazyseq for the following.
    ;; This is probably not something to be relied on.
    (map continuation structure)))

;;; New `transform` tests

(fact "transform applies functions"
  ;; Notice that, unlike `select`, it does *not* wrap results in a vector"
  (transform [:a] inc {:a 1}) => {:a 2}
  (transform [odd?] inc 1) => 2
  (transform [odd?] inc 2) => 2
  (transform [ALL] inc [1 2 3]) => [2 3 4])

(fact "applies transform only to path endpoints"
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
  ;; The following is not true of our implementation.
  ;; In the real Specter, it's special-cased, probably for efficiency.
  ;; (transform [ALL] inc nil) => nil
  )

(fact "predicates are happy with `nil` results"
  (transform [even?] (constantly nil) 2) => nil
  (transform [vector? ALL even?] (constantly nil) [2]) => [nil])

(fact "ALL produces a `seq`, not a `vector`"
  (let [result (transform [ALL] inc (list 1 2 3))]
    result => seq?
    result =not=> vector?))

;;; Old `select` tests


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

(fact "the two forms normally return specifically vectors"
  (select [:a :b] {:a {:b 1}}) => vector?
  (select [odd?] 1) => vector?)

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


  (fact "ALL applies the rest of the path to each element"
    (select [ALL :a] [{:a 1} {:a 2} {   }])
    =>               [1      2   nil ]
    (select [ALL even?] [1 2 3 4])
      =>                      [  2   4]
      (select [ALL :a even?] [{:a 1} {:a 2}])
      =>                     [           2 ])

  (fact "ALL returns vectors"
    (select [ALL] '(1 2 3)) => vector?
    (select [ALL even?] [1 2 3]) => vector?))
