(ns clj-gson.test.json
    (:require [clj-gson.gson :as gson])
    (:use [clj-gson.json]
          [clojure.test]))

(deftest test-to-json
    (binding [gson/*gson-config* {:clojure-type-adapters {:flags #{:force-serialize-map-keys-with-gson}}}]

         (testing "vector"
            (let [expected "[]"
                  result (to-json [])]
                  (is (= expected result))))

         (testing "map"
            (let [expected "{}"
                  result (to-json {})]
                 (is (= expected result))))

         (testing "string"
            (let [expected "\"string\""
                  result (to-json "string")]
                 (is (= expected result))))

         (testing "number"
            (let [expected "123.6789"
                  result (to-json 123.6789)]
                 (is (= expected result))))

         (testing "boolean"
            (let [expected "true"
                  result (to-json true)]
                 (is (= expected result))))

         (testing "map-with-named-keys"
            (let [expected "{\"a\":true}"
                  result (to-json {:a true})]
                 (is (= expected result))))

         (testing "complex-map"
            (let [expected "{\"abc\":[9,\"str\",{\"7\":6}],\"123\":\"world\",\"true\":false,\"key\":\"hello\"}"
                  result (to-json {:key "hello"
                                   123 "world"
                                   true false
                                   "abc" [9 "str" {7 6}]})]
                 (is (= expected result))))))

(deftest test-from-json
    (binding [gson/*gson-config* {:clojure-type-adapters {:flags #{:deserialize-map-keys-as-keywords}}}]
    (testing "vector"
        (let [expected []
              result (from-json "[]")]
             (is (= expected result))))

    (testing "map"
        (let [expected {}
              result (from-json "{}")]
             (is (= expected result))))

    (testing "string"
        (let [expected "string"
              result (from-json "\"string\"")]
             (is (= expected result))))

    (testing "number"
        (let [expected 123.6789
              result (from-json "123.6789")]
             (is (= expected result))))

    (testing "boolean"
        (let [expected true
              result (from-json "true")]
             (is (= expected result))))

    (testing "map-with-named-keys"
        (let [expected {:a true}
              result (from-json "{\"a\":true}")]
             (is (= expected result))))

    (testing "complex-map"
        (let [expected {:key "hello"
                        :true false
                        :abc [9.0 "str" {:7 6.0}]
                        :123 "world"}
              result (from-json "{\"abc\":[9,\"str\",{\"7\":6}],\"key\":\"hello\",\"true\":false,\"123\":\"world\"}")]
             (is (= expected result))))))