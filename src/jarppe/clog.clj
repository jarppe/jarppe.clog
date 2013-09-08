(ns jarppe.clog
  (:require [clojure.string :as s]
            [clj-stacktrace.repl :as stacktrace])
  (:import [org.joda.time.format DateTimeFormat DateTimeFormatter]
           [java.io ByteArrayOutputStream PrintWriter]))

(set! *warn-on-reflection* true)

(def levels (zipmap [:trace :debug :info :warn :error :fatal] (range)))
(def limit (atom (:trace levels)))
(def ^DateTimeFormatter time-fmt (DateTimeFormat/forPattern "yyyy/MM/dd HH:mm:ss.SSS"))

(defn set-limit! [level]
  (when-not (levels level) (throw (IllegalArgumentException. (str "unknown log elevel:" level))))
  (reset! limit (levels level)))

(defn join-args ^PrintWriter [^PrintWriter out args]
  (let [e (when (instance? Throwable (first args)) (first args))] 
    (doseq [arg (if e (rest args) args)]
      (.print out (str arg))
      (.print out \space))
    (.print out \newline)
    (when e
      (stacktrace/pst-on out false e)))
  out)

(defn make-log-message ^bytes [file line level format-args & args]
  (let [buffer (ByteArrayOutputStream. 256)
        out (PrintWriter. buffer)
        e (when (instance? Throwable (first args)) (first args))
        args (if e (rest args) args)]
    (doto out 
      (.print (name level))
      (.print \space)
      (.print (.print time-fmt (System/currentTimeMillis)))
      (.print " [")
      (.print file)
      (.print \:)
      (.print line)
      (.print "]: "))
    (.println out (if format-args
                    (apply format args)
                    (s/join \space args)))
    (when e
      (stacktrace/pst-on out false e))
    (.flush out)
    (.toByteArray buffer)))

(defmacro -log [level file line format-args & args]
  `(when (>= (levels ~level) (deref limit))
     (.write System/out (make-log-message ~file ~line ~level ~format-args ~@args))
     (.flush System/out)))

(defmacro log [level & args] `(-log ~level ~*file* ~(:line (meta &form)) false ~@args))
(defmacro logf [level & args] `(-log ~level ~*file* ~(:line (meta &form)) true ~@args))

(defmacro trace [& args] `(-log :trace ~*file* ~(:line (meta &form)) false ~@args))
(defmacro debug [& args] `(-log :debug ~*file* ~(:line (meta &form)) false ~@args))
(defmacro info  [& args] `(-log :info  ~*file* ~(:line (meta &form)) false ~@args))
(defmacro warn  [& args] `(-log :warn  ~*file* ~(:line (meta &form)) false ~@args))
(defmacro error [& args] `(-log :error ~*file* ~(:line (meta &form)) false ~@args))
(defmacro fatal [& args] `(-log :fatal ~*file* ~(:line (meta &form)) false ~@args))

(defmacro tracef [& args] `(-log :trace ~*file* ~(:line (meta &form)) true ~@args))
(defmacro debugf [& args] `(-log :debug ~*file* ~(:line (meta &form)) true ~@args))
(defmacro infof  [& args] `(-log :info  ~*file* ~(:line (meta &form)) true ~@args))
(defmacro warnf  [& args] `(-log :warn  ~*file* ~(:line (meta &form)) true ~@args))
(defmacro errorf [& args] `(-log :error ~*file* ~(:line (meta &form)) true ~@args))
(defmacro fatalf [& args] `(-log :fatal ~*file* ~(:line (meta &form)) true ~@args))
