(defproject com.spoon16/clj-gson "0.0.1"
  :description "Clojure bindings for Gson. Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Gson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of. Gson homepage; http://code.google.com/p/google-gson/"
  :url "http://github.com/spoon16/clj-gson"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.google.code.gson/gson "2.2.2"]]
  :warn-on-reflection true
  :repl-options { :init-ns clj-gson.json }
  :bootclasspath true
  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]
  :java-source-paths ["src/main/java"]
  :jar-name "clj-gson.jar"
  :compile-path "target/classes"
  :target-path "target/")