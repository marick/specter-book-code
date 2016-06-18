(ns specter-reference.select-kw-plus-pred
  ;; Specter is broken into two namespaces, one for macros, because of (1) ClojureScript and
  ;; (2) code that provides even better performance. In real life, you will probably want
  ;; to combine the two namespaces into a "catchall" namespace, using either the
  ;; [Potempkin](https://github.com/ztellman/potemkin) package or a facade over it like
  ;; [SuchWow](http://marick.github.io/suchwow/such.immigration.html)
  (:use com.rpl.specter com.rpl.specter.macros)
  (:use midje.sweet commons.clojure.core))

(facts "combining keywords and predicates"
  (select [:a map? :b] {:a 1}) => nil
  (select [:a map? :b] {:a {:b 1}}) => [1]
  (select [:a map? :b] {:a {}}) => [nil]

  (select [map? :a] {:b 1}) => [nil]
  (select [map? :a] 1) => nil)
