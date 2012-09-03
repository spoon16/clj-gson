(ns ^{:author "Eric Schoonover (http://spoon16.com/)"
      :doc "Clojure bindings for Gson, Google's JSON parsing library.
            See https://code.google.com/p/google-gson/ and http://www.json.org/
            Use 'make-gson' to build a com.google.gson.Gson instance that may be passed to methods in the gson-clj.json ns"}
  gson-clj.gson
  (:import [com.google.gson Gson
                            GsonBuilder
                            JsonParser
                            JsonSerializer
                            JsonDeserializer]
           [clojure.lang Named]
           [com.spoon16.gson_clj NamedSerializer
                                 DynamicObject
                                 DynamicObjectTypeAdapter
                                 DynamicObjectTypeAdapter$Factory]))

;; documentation, https://github.com/spoon16/gson-clj/blob/master/gson-config.example
(def ^:dynamic *gson-config* {:clojure-type-adapters {:flags #{:deserialize-map-keys-as-keywords}}
                              :flags #{:pretty-printing
                                       :enable-complex-map-key-serialization}
                              :date-format "yyyy-MM-dd'T'HH:mm:ssZ"})
(defn- configure-clojure-type-adapters
  [^GsonBuilder gson-builder {:keys [clojure-type-adapters]}]
  (when clojure-type-adapters
    (let [flags (:flags clojure-type-adapters)
          keywordize (contains? flags :deserialize-map-keys-as-keywords)]
      (-> gson-builder
        (.registerTypeAdapterFactory (DynamicObjectTypeAdapter$Factory. keywordize))
        (.registerTypeHierarchyAdapter Named (NamedSerializer.)))))
  gson-builder)

(defmulti configure-flag identity)

;; unknown flag, ignore
(defmethod configure-flag :default
  [_ ^GsonBuilder gson-builder]
  gson-builder)

(defmethod configure-flag :pretty-printing
  [_ ^GsonBuilder gson-builder]
  (.setPrettyPrinting gson-builder))

(defmethod configure-flag :generate-non-executable-json
  [_ ^GsonBuilder gson-builder]
  (.generateNonExecutableJson gson-builder))

(defmethod configure-flag :disable-html-escaping
  [_ ^GsonBuilder gson-builder]
  (.disableHtmlEscaping gson-builder))

(defmethod configure-flag :serialize-nulls
  [_ ^GsonBuilder gson-builder]
  (.serializeNulls gson-builder))

(defmethod configure-flag :serialize-special-floating-point-values
  [_ ^GsonBuilder gson-builder]
  (.serializeSpecialFloatingPointValues gson-builder))

(defmethod configure-flag :disable-inner-class-serialization
  [_ ^GsonBuilder gson-builder]
  (.disableInnerClassSerialization gson-builder))

(defmethod configure-flag :enable-complex-map-key-serialization
  [_ ^GsonBuilder gson-builder]
  (.enableComplexMapKeySerialization gson-builder))

(defmethod configure-flag :exclude-fields-without-expose-annotation
  [_ ^GsonBuilder gson-builder]
  (.excludeFieldsWithoutExposeAnnotation gson-builder))

(defn- configure-flags
  [^GsonBuilder gson-builder {:keys [flags]}]
  (map configure-flag flags)
  gson-builder)

(defn- set-date-format
  ([^GsonBuilder gson-builder date-format]
    (.setDateFormat gson-builder date-format))
  ([^GsonBuilder gson-builder date-style time-style]
    (.setDateFormat gson-builder date-style time-style)))

(defn- configure-date-format
  [^GsonBuilder gson-builder {:keys [date-format]}]
  (when date-format
    (if (and (seq date-format) (not (string? date-format)))
      (apply (partial set-date-format gson-builder) date-format)
      (set-date-format gson-builder date-format)))
  gson-builder)

(defn make-gson
  ([]
    (make-gson *gson-config*))
  ([gson-config]
    (-> (GsonBuilder.)
        (configure-clojure-type-adapters gson-config)
        (configure-flags gson-config)
        (configure-date-format gson-config)
        (.create))))