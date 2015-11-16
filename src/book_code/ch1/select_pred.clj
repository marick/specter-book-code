(ns book-code.ch1.select-pred
  (:use midje.sweet commons.clojure.core))

(defn select-pred [selector candidate]
  (if (empty? selector)
    (vector candidate)
    (if ( (first selector) candidate)
      (select-pred (rest selector) candidate)
      nil)))

(fact "our implementation matches Specter's"
  (select-pred [odd?] 1) => [1]
  (select-pred [even?] 1) => nil
  (select-pred [integer? odd?] 1) => [1]
  (select-pred [integer? even?] 1) => nil
  (select-pred [integer? odd?] "hi") => nil)

;; Like Clojure, Midje considers sequential collections equal.
;; That is:
;;     (= [1 2 3] '(1 2 3)) => true
;; Since Specter specifically returns a vector, I've added
;; a specific type test.

(fact "the result is specifically a vector"
  (select-pred [odd?] 1) => vector?)
