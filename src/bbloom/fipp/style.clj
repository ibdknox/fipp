(ns bbloom.fipp.style
  (:require [bbloom.fipp.printer :refer (serialize serialize-node)]))

;;TODO SHOULD NOT BE MUCKING WITH serialize-node !! need a tree expander!

;; How do we tell the pretty printer that these characters have zero width?

(defmethod serialize-node ::black [[_ & children]]
  (concat [{:op :text, :text (str \u001b "[30m")}]
          (serialize children)
          [{:op :text :text (str \u001b "[0m")}]))

(defmethod serialize-node ::red [[_ & children]]
  (concat [{:op :text, :text (str \u001b "[31m")}]
          (serialize children)
          [{:op :text :text (str \u001b "[0m")}]))

(defmethod serialize-node ::yellow [[_ & children]]
  (concat [{:op :text, :text (str \u001b "[33m")}]
          (serialize children)
          [{:op :text :text (str \u001b "[0m")}]))

(defmethod serialize-node ::blue [[_ & children]]
  (concat [{:op :text, :text (str \u001b "[34m")}]
          (serialize children)
          [{:op :text :text (str \u001b "[0m")}]))

(defmethod serialize-node ::magenta [[_ & children]]
  (concat [{:op :text, :text (str \u001b "[35m")}]
          (serialize children)
          [{:op :text :text (str \u001b "[0m")}]))

(defmethod serialize-node ::cyan [[_ & children]]
  (concat [{:op :text, :text (str \u001b "[36m")}]
          (serialize children)
          [{:op :text :text (str \u001b "[0m")}]))

(defmethod serialize-node ::white [[_ & children]]
  (concat [{:op :text, :text (str \u001b "[37m")}]
          (serialize children)
          [{:op :text :text (str \u001b "[0m")}]))

(def ANSI-CODES
  {:reset              "[0m"
   :bright             "[1m"
   :blink-slow         "[5m"
   :underline          "[4m"
   :underline-off      "[24m"
   :inverse            "[7m"
   :inverse-off        "[27m"
   :strikethrough      "[9m"
   :strikethrough-off  "[29m"

   :default "[39m"
   :white   "[37m"
   :black   "[30m"
   :red     "[31m"
   :green   "[32m"
   :blue    "[34m"
   :yellow  "[33m"
   :magenta "[35m"
   :cyan    "[36m"

   :bg-default "[49m"
   :bg-white   "[47m"
   :bg-black   "[40m"
   :bg-red     "[41m"
   :bg-green   "[42m"
   :bg-blue    "[44m"
   :bg-yellow  "[43m"
   :bg-magenta "[45m"
   :bg-cyan    "[46m"
   })
