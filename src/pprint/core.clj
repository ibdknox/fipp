(ns pprint.core
  "See: Oleg Kiselyov, Simon Peyton-Jones, and Amr Sabry
  Lazy v. Yield: Incremental, Linear Pretty-printing"
  (:require [clojure.string :as s]
            [clojure.core.reducers :as r]
            [clojure.data.finger-tree :refer (double-list)]
            [pprint.transduce :as t]))

;;; Serialize document into a stream

(defmulti serialize-node first)

(defn serialize [doc]
  (cond
    (seq? doc) (mapcat serialize doc)
    (string? doc) [{:op :text, :text doc}]
    (keyword? doc) (serialize-node [doc])
    (vector? doc) (serialize-node doc)
    :else (throw (Exception. "Unexpected class for doc node"))))

(defmethod serialize-node :text [[_ & text]]
  [{:op :text, :text (apply str text)}])

(defmethod serialize-node :span [[_ & children]]
  (serialize children))

(defmethod serialize-node :line [[_ inline]]
  (let [inline (or inline " ")]
    (assert (string? inline))
    [{:op :line, :inline inline}]))

(defmethod serialize-node :group [[_ & children]]
  (concat [{:op :begin}] (serialize children) [{:op :end}]))

;TODO serialize nest & align nodes

(comment

  (serialize "apple")
  (serialize [:text "apple" "ball"])
  (serialize [:span "apple" [:group "ball" :line "cat"]])
  (serialize [:span "apple" [:line ","] "ball"])

  (def doc1 [:group "A" :line [:group "B" :line "C"]])
  (serialize doc1)

)


;;; Annotate right-side of nodes


(defn throw-op [node]
  (throw (Exception. (str "Unexpected op on node: " node))))

(def annotate-rights
  (t/map-state
    (fn [position node]
      (case (:op node)
        :text
          (let [position* (+ position (count (:text node)))]
            [position* (assoc node :right position*)])
        :line
          (let [position* (+ position (count (:inline node)))]
            [position* (assoc node :right position*)])
        :begin
          [position (assoc node :left position)] ;TODO is :left needed?
        :end
          [position (assoc node :right position)]
        (throw-op node)))
    0))

(comment

  (->> doc1 serialize annotate-rights (into []) clojure.pprint/pprint)

)




;TODO normalize -- i think that this can be done on the serialize

(def empty-deque (double-list))



(comment

  (defn dbg [x]
    (pprint x)
    x)

  (pretty [:text "word"])

  (pretty [:span "cool" :line "dog"])

  (pretty [:group "omg" :line [:text "wtf" "is" "up?"] [:nest 2 "dog?"]])

)
