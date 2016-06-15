(ns exercises.ch1.select-kw
  (:use midje.sweet commons.clojure.core))


(defn select-kw [path structure]
  :unimplemented)

(future-facts "Behaves the way specter/select does"
  (select-kw [:a] nil) => [nil]
  (select-kw [:a] :something-random) => [nil]
  (select-kw [:a] {:a 1}) => [1]
  (select-kw [:a] {:not-a 1}) => [nil]
  (select-kw [:a] {}) => [nil]

  (select-kw [:a :b] {:a {:b 1}}) => [1]
  (select-kw [:a :b] {:a 1}) => [nil]
  (select-kw [:a :b] {:a {}}) => [nil])

;; Like Clojure, Midje considers sequential collections equal.
;; That is:
;;     (= [1 2 3] '(1 2 3)) => true
;; Since Specter specifically returns a vector, I've added
;; a specific type test.

(future-fact "the result is specifically a vector"
  (select-kw [:a :b] {:a {:b 1}}) => vector?)
