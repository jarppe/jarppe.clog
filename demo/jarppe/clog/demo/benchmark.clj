(ns jarppe.clog.demo.benchmark
  (:use jarppe.clog))

(log-writer! (fn [m]))

(defn benchmark []
  (let [e (Exception. "Ups")]
    ;; Warm-up
    (dotimes [_ 100000] (debug "Oh noes"))
    ;; Test
    (time (dotimes [_ 100000] (debug "Oh noes")))))


