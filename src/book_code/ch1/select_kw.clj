(ns book-code.ch1.select-kw
  (:use midje.sweet commons.clojure.core))

(defn select-kw [selector structure]
  (if (empty? selector)
    (vector structure)
    (select-kw (rest selector) (get structure (first selector)))))

(facts "same behavior from local implementation"
  (select-kw [:a] nil) => [nil]
  (select-kw [:a] :something-random) => [nil]
  (select-kw [:a] {:a 1}) => [1]
  (select-kw [:a] {:not-a 1}) => [nil]
  (select-kw [:a] {}) => [nil]

  (select-kw [:a :b] {:a {:b 1}}) => [1]
  (select-kw [:a :b] {:a 1}) => [nil]
  (select-kw [:a :b] {:a {}}) => [nil])

(fact "the result is specifically a vector"
  (select-kw [:a :b] {:a {:b 1}}) => vector?)
