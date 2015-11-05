(ns exercises.ch1.basic-specter)

(defprotocol StructurePath
  (select* [this structure continuation]))

(extend-type clojure.lang.Keyword
  StructurePath
  (select* [this structure continuation]
    (continuation (get structure this))))

(extend-type clojure.lang.AFn
  StructurePath
  (select* [this structure continuation]
    (if (this structure)
      (continuation structure)
      nil)))


(defn element-functions-for [selector-element]
  (find-protocol-impl StructurePath selector-element))

(def selector-function-for (comp :select* element-functions-for))


(defn mkfn:selector-function-calling-continuation [element continuation]
  (let [selector-function (selector-function-for element)]
    (fn [structure]
      (selector-function element structure continuation))))


(defn mkfn:frozen-selector-actions [selector]
  (reduce (fn [continuation element]
            (mkfn:selector-function-calling-continuation element continuation))
          vector
          (reverse selector)))

(defn select [selector structure]
  (let [frozen (mkfn:frozen-selector-actions selector)]
    (frozen structure)))
