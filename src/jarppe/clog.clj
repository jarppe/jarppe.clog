(ns jarppe.clog
  (:import [org.joda.time.format DateTimeFormat DateTimeFormatter]))

(def levels (zipmap [:trace :debug :info :warn :error :fatal] (range)))
(def limit (atom (:trace levels)))
(def ^DateTimeFormatter time-fmt (DateTimeFormat/forPattern "yyyy/MM/dd HH:mm:ss.SSS"))

(defn log-limit! [level]
  (swap! limit (constantly (levels level))))

(defn time-str []
  (.print time-fmt (System/currentTimeMillis)))

(defn ^StringBuilder join-args [^StringBuilder buffer args]
  (dorun
    (map #(.append buffer (str %)) (take (- (count args) (if (instance? Throwable (last args)) 1 0)) args)))
  buffer)

(defn ^StringBuilder stacktrace [^StringBuilder buffer args]
  (let [e (last args)]
    (if (instance? Throwable e)
      (-> buffer
        (.append "   ")
        (.append (.getName (class e)))
        (.append ": ")
        (.append (.getMessage ^Throwable e))
        (.append \newline))))
  buffer)

(defn make-log-message [file line level & args]
  (-> (StringBuilder.) 
    (.append (name level))
    (.append \space)
    (.append (time-str))
    (.append \space)
    (.append \[)
    (.append file)
    (.append \:)
    (.append line)
    (.append \])
    (.append \space)
    (join-args args)
    (.append \newline)
    (stacktrace args)
    (.toString)))

(defn log-out [message]
  (print message)
  (flush))

(defmacro -log [level file line & args]
  `(if (>= (levels ~level) (deref limit))
     (log-out (make-log-message ~file ~line ~level ~@args))))

(defmacro log [level & args] `(-log ~level ~*file* ~(:line (meta &form)) ~@args))

(defmacro trace [& args] `(-log :trace ~*file* ~(:line (meta &form)) ~@args))
(defmacro debug [& args] `(-log :debug ~*file* ~(:line (meta &form)) ~@args))
(defmacro info [& args]  `(-log :info  ~*file* ~(:line (meta &form)) ~@args))
(defmacro warn [& args]  `(-log :warn  ~*file* ~(:line (meta &form)) ~@args))
(defmacro error [& args] `(-log :error ~*file* ~(:line (meta &form)) ~@args))
(defmacro fatal [& args] `(-log :fatal ~*file* ~(:line (meta &form)) ~@args))
