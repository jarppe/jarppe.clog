jarppe.clog
===========

Simple but efficient Clojure logging library.

Someone once said that every developer will create his/her own logging framework at least once. I think would could add to that *"…for each language he/she learns"*. So here's my very own logging library for Clojure.

# But why yet another log lib?

There already exists multiple well tested, well documented, log libraries for Java and the [clojure/tools.logging](https://github.com/clojure/tools.logging) wraps them nicely. If you are looking for production quality logging solution, use **clojure/tools.logging**. Also, **jarppe.clog** is not as feature rich (by far) as more commonly used logging frameworks. 

The main difference between **clojure/tools.logging** and **jarppe.clog** is that **jarppe.clog** adds source file name and line number to the logging calls at read time. The Java libraries used by **clojure/tools.logging** do the same at runtime by creating a Java exception and examining the stacktrace information in it.

# Dependencies

My goal is to use this with ClojureScript too, so I'll try to drop all dependencies to any Java libraries. Currently there is a dependency to [Joda-Time](http://joda-time.sourceforge.net) but I'll propably drop that in future.

## Install

```
$ clone https://github.com/jarppe/jarppe.clog.git
$ cd jarppe.clog
$ lein install
```

## Use

Add `[jarppe/clog "0.1.0"]` to you lein dependencies.

## Example

```clojure
(ns hello
  (:use [jarppe.clog]))

(debug "Hello")
(info "Answer is: " 42)
(error "Oh no, something broke" (Exception. "Not my fault"))
```

Code above produces:

```
debug 2012/09/08 17:15:47.666 [jarppe/example.clj:4] Hello
info 2012/09/08 17:15:47.667 [jarppe/example.clj:5] Answer is: 42
error 2012/09/08 17:15:47.668 [jarppe/example.clj:6] Oh no, something broke
   java.lang.Exception: Not my fault
```

As you can see, if the last argument is an exception it will be printed on next line. I'll add a full stacktrace (propably) soon.

Testing...

## License

Copyright © 2012 Jarppe Länsiö

Distributed under the Eclipse Public License, the same as Clojure.
