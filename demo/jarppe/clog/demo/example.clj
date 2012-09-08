(ns jarppe.clog.demo.example
  (:use [jarppe.clog]))

(debug "Hello")

(info "Answer is: " 42)

(error "Oh no, something broke" (Exception. "Not my fault"))
