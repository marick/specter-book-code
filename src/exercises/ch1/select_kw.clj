(ns exercises.ch1.select-kw
  (:use midje.sweet commons.clojure.core))


(defn select-kw [selector structure]
  )

(future-facts "Behaves the way specter/select does"
  (select-kw [:a] nil) => [nil]
  (select-kw [:a] :something-random) => [nil]
  (select-kw [:a] {:a 1}) => [1]
  (select-kw [:a] {:not-a 1}) => [nil]
  (select-kw [:a] {}) => [nil]

  (select-kw [:a :b] {:a {:b 1}}) => [1]
  (select-kw [:a :b] {:a 1}) => [nil]
  (select-kw [:a :b] {:a {}}) => [nil])
