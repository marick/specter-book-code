(ns book-code.ch1.realistic
  (:use midje.sweet commons.clojure.core))



(defprotocol StructurePath
  (select* [this structure continuation]))

(extend-type clojure.lang.Keyword
  StructurePath
  (select* [this structure continuation]
    (continuation (get structure this))))

(extend-type clojure.lang.AFn
  StructurePath
  (select* [this structure continuation]
    (if (this structure)
      (continuation structure)
      nil)))


(defn element-functions-for [selector-element]
  (find-protocol-impl StructurePath selector-element))

(def selector-function-for (comp :select* element-functions-for))


(defn mkfn:selector-function-calling-continuation [element continuation]
  (let [selector-function (selector-function-for element)]
    (fn [structure]
      (selector-function element structure continuation))))


(defn mkfn:frozen-selector-actions [selector]
  (reduce (fn [continuation element]
            (mkfn:selector-function-calling-continuation element continuation))
          vector
          (reverse selector)))

(defn select [selector structure]
  (let [frozen (mkfn:frozen-selector-actions selector)]
    (frozen structure)))




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
