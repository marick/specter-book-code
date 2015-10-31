(ns fun.core)


(defn select-pred [selector candidate]
  (cond (empty? selector)
        (vector candidate)

        ( (first selector) candidate)
        (select-pred (rest selector) candidate)

        :else
        nil))


(defn select-pred [selector candidate]
  (if (empty? selector)
    (vector candidate)
    (if ( (first selector) candidate)
      (select-pred (rest selector) candidate)
      nil)))


(defn select-both [[x & xs :as selector] structure]
  (cond (empty? selector)
        (vector structure)

        (keyword? x)
        (select-both xs (get structure x))

        (fn? x)
        (if (x structure)
          (select-both xs structure)
          nil)))

(defprotocol StructurePath
  (select* [this remainder structure]))

(declare select-both)

(extend-type clojure.lang.Keyword
  StructurePath
  (select* [this remainder structure]
    (select-both remainder (get structure this))))

(extend-type clojure.lang.AFn
  StructurePath
  (select* [this remainder structure]
    (if (this structure)
      (select-both remainder structure)
      nil)))

(defn select-both [[x & xs :as selector] structure]
  (if (empty? selector)
    (vector structure)
    (select* x xs structure)))


;; (require '[com.rpl.specter.impl :as impl])

;; (prn StructurePath)
;; (prn (impl/structure-path-impl :key))


;; (defn selector-element-functions-for [selector]
;;   (->> selector
;;        (map (partial find-protocol-impl StructurePath))
;;        (map :select*)))

;; (prn (selector-element-functions-for [:a map? :b]))
