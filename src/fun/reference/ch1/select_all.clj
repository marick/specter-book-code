(ns fun.reference.ch1.select-pred
  (:require [com.rpl.specter :as s])
  (:use midje.sweet commons.clojure.core))

(fact
  (s/select [ALL] [1]) => [1]
  (s/select [ALL ALL] [ [1] [2 3] ]) => [1 2 4])
