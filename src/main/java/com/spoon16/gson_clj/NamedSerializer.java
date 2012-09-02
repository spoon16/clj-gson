package com.spoon16.gson_clj;

import java.lang.reflect.Type;  

import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;

import clojure.lang.Named;

/**
 * Serialize only the name types that implement {@link Named}. At time of writing affected types include {@link clojure.lang.Keyword} and {@link clojure.lang.Symbol}.
 */
public class NamedSerializer implements JsonSerializer<Named> {
    @Override
    public JsonElement serialize( final Named named, final Type typeOf, final JsonSerializationContext context ) {
        if ( named == null ) {
            return JsonNull.INSTANCE;
        }

        return context.serialize( named.getName() );
    }
}