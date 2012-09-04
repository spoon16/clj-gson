(ns ^{:author "Eric Schoonover"
      :doc "Clojure bindings for Gson, Google's JSON parsing library.
            See https://code.google.com/p/google-gson/ and http://www.json.org/
            Use 'to-json' or 'to-json-as' to serialize an object as a JSON string.
            Use 'to-json-tree' or 'to-json-tree-as' to serialize an object as a com.google.gson.JsonElement.
            Use 'from-json' or 'from-json-as' to deserialize an object from JSON source.
            Use 'parse-json-tree' to read JSON source into a com.google.gson.JsonElement."}
  clj-gson.json
  (:require [clj-gson.gson :as gson])
  (:import [com.google.gson Gson
                            JsonParser]
           [com.spoon16.clj_gson DynamicObject
                                 DynamicObjectTypeAdapter]))

(defn parse-json-tree
  [json-source]
  (-> (JsonParser.)
      (.parse json-source)))

(defn to-json-tree-as
  ([obj serialize-as]
    (to-json-tree-as (gson/make-gson) serialize-as))
  ([^Gson gson obj serialize-as]
    (.toJsonTree gson obj serialize-as)))

(defn to-json-tree
  ([obj]
    (to-json-tree (gson/make-gson) obj))
  ([^Gson gson obj]
    (to-json-tree-as gson obj (type obj))))

(defn to-json-as
  ([obj serialize-as]
    (to-json-as (gson/make-gson) obj serialize-as))
  ([^Gson gson obj serialize-as]
    (.toJson gson obj serialize-as)))

(defn to-json
  ([obj]
  	(to-json (gson/make-gson) obj))
  ([^Gson gson obj]
  	(to-json-as gson obj (type obj))))

(defn from-json-as
  ([json-source deserialize-as]
    (from-json-as (gson/make-gson) json-source deserialize-as))
  ([^Gson gson json-source deserialize-as]
    (.fromJson gson json-source deserialize-as)))

(defn- are-clojure-type-adapters-registered?
  [^Gson gson]
  (instance? DynamicObjectTypeAdapter (.getAdapter gson DynamicObject)))

(defn from-json
  ([json-source]
   	(from-json (gson/make-gson) json-source))
  ([^Gson gson json-source]
    ;; prefer deserialization via DynamicObjectTypeAdapter over the Gson provided ObjectTypeAdapter
    (if (are-clojure-type-adapters-registered? gson)
      (-> (from-json-as gson json-source DynamicObject)
          (.getValue))
      (from-json-as gson json-source Object))))