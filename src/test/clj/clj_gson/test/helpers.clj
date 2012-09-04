(ns clj-gson.test.helpers
  (:use [clojure.pprint :only [pprint]])
  (:import [com.google.gson Gson
                            GsonBuilder]))

(defn print-gson-builder
  [gson-builder]
  (pprint (.toJson (Gson.) gson-builder)))