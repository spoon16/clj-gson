{
;; ---------- clj-gson Configuration 

;; The presence of the :clojure-type-adapters flag will register the type 
;; adapters that ensure Clojure types are serialized and deserialized cleanly.
   :clojure-type-adapters {:flags #{:deserialize-map-keys-as-keywords}}

;; ---------- Gson Configuration
;; detailed information about Gson configuration
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html
;; ----------

   :flags #{
;; Configures Gson to output Json that fits in a page for pretty printing.
;; This option only affects Json serialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#setPrettyPrinting()
            :pretty-printing

;; Makes the output JSON non-executable in Javascript by prefixing the 
;; generated JSON with some special text. This prevents attacks from third-
;; party sites through script sourcing.
;;
;; See http://code.google.com/p/google-gson/issues/detail?id=42 for details.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#generateNonExecutableJson()
            :generate-non-executable-json

;; By default, Gson escapes HTML characters such as <, >, etc. Use this 
;; option to configure Gson to pass-through HTML characters as is.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#disableHtmlEscaping()
            :disable-html-escaping

;; Configure Gson to serialize null fields. By default, Gson omits all fields 
;; that are null during serialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#serializeNulls()
            :serialize-nulls

;; Section 2.4 of http://www.ietf.org/rfc/rfc4627.txt (JSON specification) 
;; disallows special double values (NaN, Infinity, -Infinity). However, 
;; http://www.ecma-international.org/publications/files/ECMA-ST/Ecma-262.pdf 
;; (JavaScript Specification see section 4.3.20, 4.3.22, 4.3.23) allows these 
;; values as valid Javascript values. Moreover, most JavaScript engines will 
;; accept these special values in JSON without problem. So, at a practical 
;; level, it makes sense to accept these values as valid JSON even though JSON 
;; specification disallows them.
;; 
;; Gson always accepts these special values during deserialization. However, 
;; it outputs strictly compliant JSON. Hence, if it encounters a float value 
;; Float/NaN, Float/POSITIVE_INFINITY, Float/NEGATIVE_INFINITY, or a double 
;; value Double/NaN, Double/POSITIVE_INFINITY, Double/NEGATIVE_INFINITY, it 
;; will throw an IllegalArgumentException. This method provides a way to 
;; override the default behavior when you know that the JSON receiver will be 
;; able to handle these special values.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#serializeSpecialFloatingPointValues()
            :serialize-special-floating-point-values

;; Configures Gson to exclude inner classes during serialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#disableInnerClassSerialization()
            :disable-inner-class-serialization

;; Enabling this feature will only change the serialized form if the map key 
;; is a complex type (i.e. non-primitive) in its serialized JSON form.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#enableComplexMapKeySerialization()
            :enable-complex-map-key-serialization

;; Configures Gson to exclude all fields from consideration for serialization 
;; or deserialization that do not have the Expose annotation.
;;
;; Named instances (Keywords, Symbols) will not serialize as primitive Strings
;; if this is not enabled.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#excludeFieldsWithoutExposeAnnotation()
            :exclude-fields-without-expose-annotation}

;; Configures Gson to excludes all class fields that have the specified 
;; modifiers.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#excludeFieldsWithModifiers(int...)
   :exclude-fields-with-modifiers []

   :exclusion-strategies {
;; Configures Gson to apply the passed in exclusion strategy during 
;; serialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#addSerializationExclusionStrategy(com.google.gson.ExclusionStrategy)
                          :serialization []

;; Configures Gson to apply the passed in exclusion strategy during 
;; deserialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#addDeserializationExclusionStrategy(com.google.gson.ExclusionStrategy)
                          :deserialization []

;; Configures Gson to apply a set of exclusion strategies during both 
;; serialization and deserialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#setExclusionStrategies(com.google.gson.ExclusionStrategy...)
                          :both []}

;; Configures Gson for custom serialization or deserialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#registerTypeAdapter(java.lang.reflect.Type, java.lang.Object)
   :type-adapters []

;; Register a factory for type adapters.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#registerTypeHierarchyAdapter(java.lang.Class, java.lang.Object)
   :type-adapter-factories []

;; Configures Gson for custom serialization or deserialization for an 
;; inheritance type hierarchy.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#registerTypeHierarchyAdapter(java.lang.Class, java.lang.Object)
   :type-heirarchy-adapters []

;; Configures Gson to serialize Date objects according to the pattern provided.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#setDateFormat(java.lang.String)
   :date-format "yyyy-MM-dd'T'HH:mm:ssZ"

   :field-naming {

;; Configures Gson to apply a specific naming policy to an object's field 
;; during serialization and deserialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#setFieldNamingPolicy(com.google.gson.FieldNamingPolicy)
                  :policy (FieldNamingPolicy/LOWER_CASE_WITH_DASHES)

;; Configures Gson to apply a specific naming policy strategy to an object's 
;; field during serialization and deserialization.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#setFieldNamingStrategy(com.google.gson.FieldNamingStrategy)
                  :strategy nil} 

;; Configures Gson to apply a specific serialization policy for Long and long 
;; objects.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#setLongSerializationPolicy(com.google.gson.LongSerializationPolicy)
   :long-serialization-policy (LongSerializationPolicy/STRING)

;; Configures Gson to enable versioning support.
;;
;; http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/GsonBuilder.html#setVersion(double)
   :version 2.0
}