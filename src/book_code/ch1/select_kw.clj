(ns book-code.ch1.select-kw
  (:use midje.sweet commons.clojure.core))

(defn select-kw [path structure]
  (if (empty? path)
    (vector structure)
    (select-kw (rest path) (get structure (first path)))))

(facts "same behavior from local implementation"
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

(fact "the result is specifically a vector"
  (select-kw [:a :b] {:a {:b 1}}) => vector?)
