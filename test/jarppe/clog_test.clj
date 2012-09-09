(ns jarppe.clog-test
  (:use jarppe.clog
        clojure.test
        midje.sweet))

(facts
  (fact (-> (join-args (StringBuilder.) ["a" "b" "c"]) .toString)  => "abc")
  (fact (-> (join-args (StringBuilder.) ["a" 42 "c"]) .toString)  => "a42c")
  (fact (-> (join-args (StringBuilder.) ["a" "b" "c" (Exception. "Oh noes")]) .toString)  => "abc"))

(facts
  (fact (-> (stacktrace (StringBuilder.) ["a" "b" "c"]) .toString) => "")
  (fact (-> (stacktrace (StringBuilder.) ["a" "b" "c" (Exception. "Oh noes")]) .toString) => (has-prefix "   java.lang.Exception: Oh noes\n")))

(facts
  (fact (make-log-message "foo" 42 :fatal "a" "b" "c") => (has-prefix "fatal 2012/09/08 16:11:37.592 [foo:42] abc\n")
        (provided (time-str) => "2012/09/08 16:11:37.592")))
