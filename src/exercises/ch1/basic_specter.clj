(ns exercises.ch1.basic-specter
  "This is used by other namespaces so that the grotty details of looking up
   protocol functions aren't always visible.")

(defprotocol Navigator
  (select* [this structure continuation]))

(extend-type clojure.lang.Keyword
  Navigator
  (select* [this structure continuation]
    (continuation (get structure this))))

(extend-type clojure.lang.AFn
  Navigator
  (select* [this structure continuation]
    (if (this structure)
      (continuation structure)
      nil)))


(defn element-functions-for [selector-element]
  (find-protocol-impl Navigator selector-element))

(def selector-function-for (comp :select* element-functions-for))


(defn mkfn:selector-function-calling-continuation [element continuation]
  (let [selector-function (selector-function-for element)]
    (fn [structure]
      (selector-function element structure continuation))))


(defn predict-select-computation [selector]
  (reduce (fn [continuation element]
            (mkfn:selector-function-calling-continuation element continuation))
          vector
          (reverse selector)))

(defn select [selector structure]
  ((predict-select-computation selector) structure))
