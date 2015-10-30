(ns fun.book.ch1.select-kw
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))


(facts "keyword selectors in Spector"
  (s/select [:a] nil) => [nil]
  (s/select [:a] :something-random) => [nil]
  (s/select [:a] {:a 1}) => [1]
  (s/select [:a] {:not-a 1}) => [nil]
  (s/select [:a] {}) => [nil]

  (s/select [:a :b] {:a {:b 1}}) => [1]
  (s/select [:a :b] {:a 1}) => [nil]
  (s/select [:a :b] {:a {}}) => [nil])

(defn select-kw [[this & continued-selecting :as selector] structure]
  (if (empty? selector)
    (vector structure)
    (select-kw continued-selecting (get structure this))))

(facts "same behavior from local implementation"
  (select-kw [:a] nil) => [nil]
  (select-kw [:a] :something-random) => [nil]
  (select-kw [:a] {:a 1}) => [1]
  (select-kw [:a] {:not-a 1}) => [nil]
  (select-kw [:a] {}) => [nil]

  (select-kw [:a :b] {:a {:b 1}}) => [1]
  (select-kw [:a :b] {:a 1}) => [nil]
  (select-kw [:a :b] {:a {}}) => [nil])
