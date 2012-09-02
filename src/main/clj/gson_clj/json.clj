(ns ^{:author "Eric Schoonover"
       :doc "Clojure bindings for Gson, Googles JSON parsing library.
            See http://www.json.org/
            Use 'to-json' or 'to-json-as' to serialize an object as a JSON string.
            Use 'from-json' or 'from-json-as' to deserialize an object from JSON source.
            Use 'parse-json' to parse JSON source into a tree with a com.google.gson.JsonElement root"}
  gson-clj.json
  (:import [com.google.gson Gson
                            GsonBuilder
                            JsonParser
                            JsonSerializer
                            JsonDeserializer
                            JsonArray]
           [clojure.lang Named]
           [com.spoon16.gson_clj NamedSerializer
                                 DynamicObject
                                 DynamicObjectTypeAdapter
                                 DynamicObjectTypeAdapter$Factory]))

(def ^:dynamic *gson-config* {
                            ;; ---------- Gson-CLJ Configuration 
                               :clojure-type-adapters { :enabled true
                                                        :keywordize? true }

                            ;; ---------- Gson Configuration
                            ;; detailed information about Gson configuration http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html
                            ;; ----------
                            ;; :exclusion-strategies { :serialization []
                            ;;                         :deserialization []
                            ;;                         :both [] }
                            ;; :disable-html-escaping false
                               :disable-inner-class-serialization true
                            ;; :enable-complex-map-key-serialization false
                            ;; :exclude-fields-with-modifiers []
                            ;; :exclude-fields-without-expose-annotation false
                            ;; :generate-non-executable-json false
                            ;; :type-adapters []
                            ;; :type-adapter-factories []
                            ;; :type-heirarchy-adapters []
                            ;; :serialize-nulls false
                            ;; :serialize-special-floating-point-values false
                               :date-format "yyyy-MM-dd'T'HH:mm:ssZ"
                            ;; :field-naming { :policy
                            ;;                 :strategy } 
                            ;; :long-serialization-policy
                               :enable-pretty-printing true
                            ;; :version ##
                            })

(defn configure-clojure-type-adapters
  [^GsonBuilder gson-builder {{:keys [enabled keywordize?]} :clojure-type-adapters}]
  (when enabled
    (-> gson-builder
      (.registerTypeAdapterFactory (DynamicObjectTypeAdapter$Factory. (boolean keywordize?)))
      (.registerTypeHierarchyAdapter Named (NamedSerializer.))))
  gson-builder)

(defn configure-inner-class-serialization
  [^GsonBuilder gson-builder {:keys [disable-inner-class-serialization]}]
  (when disable-inner-class-serialization
    (.disableInnerClassSerialization gson-builder))
  gson-builder)

(defn configure-date-format
  [^GsonBuilder gson-builder {:keys [date-format]}]
  (when date-format
    (.setDateFormat gson-builder date-format))
  gson-builder)

(defn configure-pretty-printing
  [^GsonBuilder gson-builder {:keys [enable-pretty-printing]}]
  (when enable-pretty-printing
    (.setPrettyPrinting gson-builder))
  gson-builder)

(defn make-gson
  ([]
    (make-gson *gson-config*))
  ([gson-config]
    (-> (GsonBuilder.)
        (configure-clojure-type-adapters gson-config)
        (configure-inner-class-serialization gson-config)
        (configure-date-format gson-config)
        (configure-pretty-printing gson-config)
        (.create))))

(defn to-json-as
  ([obj serialize-as]
    (to-json-as (make-gson) obj serialize-as))
  ([^Gson gson obj serialize-as]
    (.toJsonTree gson obj serialize-as)))

(defn to-json
  ([obj]
  	(to-json (make-gson) obj))
  ([^Gson gson obj]
  	(str (to-json-as gson obj (type obj)))))

(defn from-json-as
  ([json-source deserialize-as]
    (from-json-as (make-gson) json-source deserialize-as))
  ([^Gson gson json-source deserialize-as]
    (.fromJson gson json-source deserialize-as)))

(defn- is-default-type-adapter-registered?
  [^Gson gson]
  (instance? DynamicObjectTypeAdapter (.getAdapter gson DynamicObject)))

(defn from-json
  ([json-source]
   	(from-json (make-gson) json-source))
  ([^Gson gson json-source]
    ;; prefer deserialization via DynamicObjectTypeAdapter over the Gson provided ObjectTypeAdapter
    (if (is-default-type-adapter-registered? gson)
      (-> (from-json-as gson json-source DynamicObject)
          (.getValue))
      (from-json-as gson json-source Object))))

(defn parse-json
  [json-source]
  (-> (JsonParser.)
      (.parse json-source)))