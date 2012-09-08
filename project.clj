(defproject jarppe.clog "0.0.1-SNAPSHOT"
  :description "jarppe.clog"
  :url "http://github.com/jarppe/jarppe.clog"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [joda-time/joda-time "2.1"]]
  :profiles {:dev {:dependencies [[midje "1.4.0" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-midje "2.0.0-SNAPSHOT"]]}}
  :warn-on-reflection true
  :main jarppe.clog.core
  :min-lein-version "2.0.0")
