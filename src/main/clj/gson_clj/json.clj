(ns ^{:author "Eric Schoonover"
       :doc "Clojure bindings for Gson, Googles JSON parsing library.
            See https://code.google.com/p/google-gson/ and http://www.json.org/
            Use 'to-json' or 'to-json-as' to serialize an object as a JSON string.
            Use 'from-json' or 'from-json-as' to deserialize an object from JSON source.
            Use 'parse-json' to parse JSON source into a tree typed com.google.gson.JsonElement"}
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
                            ;; :disable-html-escaping true ;; default false
                               :disable-inner-class-serialization true ;; defualt false
                               :enable-complex-map-key-serialization true ;; default false, the default Gson Map TypeAdapter is a bit annoying in that only
                                                                          ;;                if this is enabled will keys be serialized via their registered
                                                                          ;;                type adapter, if this is not enabled than String.valueOf is called
                                                                          ;;                for each key in the target map.  I would have preferred if 
                                                                          ;;                String.valueOf were a fallback in the case that the registered
                                                                          ;;                TypeAdapter did not return a JsonPrimitive
                            ;; :exclude-fields-with-modifiers [] ;; default []
                            ;; :exclude-fields-without-expose-annotation true ;; default false
                            ;; :generate-non-executable-json true ;; default false
                            ;; :type-adapters [] ;; default []
                            ;; :type-adapter-factories [] ;; default []
                            ;; :type-heirarchy-adapters [] ;; default []
                            ;; :serialize-nulls true ;; default false
                            ;; :serialize-special-floating-point-values true ;; default false
                               :date-format "yyyy-MM-dd'T'HH:mm:ssZ" ;; default nil
                            ;; :field-naming { :policy (FieldNamingPolicy/LOWER_CASE_WITH_DASHES) ;; default FieldNamingPolicy/IDENTITY
                            ;;                 :strategy nil ;; default nil
                            ;;               } 
                            ;; :long-serialization-policy (LongSerializationPolicy/STRING) ;; default LongSerializationPolicy/DEFAULT
                               :enable-pretty-printing true ;; default false
                            ;; :version ##
                            })

(defn configure-complex-map-key-serialization
  [^GsonBuilder gson-builder {:keys [enable-complex-map-key-serialization]}]
  (when enable-complex-map-key-serialization
    (.enableComplexMapKeySerialization gson-builder))
  gson-builder)

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
  [^GsonBuilder gson-builder {:keys [^String date-format]}]
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

(defn- are-clojure-type-adapters-registered?
  [^Gson gson]
  (instance? DynamicObjectTypeAdapter (.getAdapter gson DynamicObject)))

(defn from-json
  ([json-source]
   	(from-json (make-gson) json-source))
  ([^Gson gson json-source]
    ;; prefer deserialization via DynamicObjectTypeAdapter over the Gson provided ObjectTypeAdapter
    (if (are-clojure-type-adapters-registered? gson)
      (-> (from-json-as gson json-source DynamicObject)
          (.getValue))
      (from-json-as gson json-source Object))))

(defn parse-json
  [json-source]
  (-> (JsonParser.)
      (.parse json-source)))