(ns book-code.ch1.klunky-protocols
  (:use midje.sweet commons.clojure.core))


(defprotocol StructurePath
  (select* [this remainder structure]))

(defn select [[x & xs :as selector] structure]
  (if (empty? selector)
    (vector structure)
    (select* x xs structure)))

(extend-type clojure.lang.Keyword
  StructurePath
  (select* [this remainder structure]
    (select remainder (get structure this))))

(extend-type clojure.lang.AFn
  StructurePath
  (select* [this remainder structure]
    (if (this structure)
      (select remainder structure)
      nil)))



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
