package com.spoon16.clj_gson;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

/**
 * The Map TypeAdapter that Gson uses by default will only convert JsonObject
 * attribute names using the TypeAdapter associated with the keys Type if 
 * ComplexMapKeySerialization is enabled. If 
 */
class DynamicObjectSerializer {
    private final Gson gson;
    private final boolean forceSerializeMapKeysWithGson;

    public static JsonElement serialize( final Gson gson, final boolean forceSerializeMapKeysWithGson, final Object value ) {
        return new DynamicObjectSerializer( gson, forceSerializeMapKeysWithGson ).serialize( value );
    }

    private DynamicObjectSerializer( final Gson gson, final boolean forceSerializeMapKeysWithGson ) {
        this.gson = gson;
        this.forceSerializeMapKeysWithGson = forceSerializeMapKeysWithGson;
    }

    public JsonElement serialize( final Object value ) {
        if( !forceSerializeMapKeysWithGson ) {
            return gson.toJsonTree( value );
        }

        final JsonElement result = writeObject( value );
        return result;
    }

    private JsonElement writeObject( final Object value ) {
        if ( value instanceof Map ) {
            final Map<Object, Object> map = (Map<Object, Object>) value;
            return writeObject( map );
        } else if ( value instanceof Iterable ) {
            final Iterable<Object> iterable = (Iterable<Object>) value;
            return writeArray( iterable );
        }

        return gson.toJsonTree( value );
    }

    private JsonElement writeObject( final Map<Object, Object> map ) {
        final Map<String, Object> outputMap = makeOutputMap( map );
        return gson.toJsonTree( outputMap );
    }

    private Map<String, Object> makeOutputMap( final Map<Object, Object> map ) {
        final Map<String, Object> outputMap = new HashMap<String, Object>();
        for( final Map.Entry<Object, Object> entry : map.entrySet() ) {
            final String key = makeStringKey( entry.getKey() );
            final Object value = getOutputObject( entry.getValue() );
            outputMap.put( key, value );
        }

        return outputMap;
    }

    private String makeStringKey( final Object key ) {
        final JsonElement keyElement = gson.toJsonTree( key );
        if ( keyElement.isJsonPrimitive() ) {
            final JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
            if ( primitive.isNumber() ) {
              return String.valueOf(primitive.getAsNumber());
            } else if ( primitive.isBoolean() ) {
              return Boolean.toString(primitive.getAsBoolean());
            } else if ( primitive.isString() ) {
              return primitive.getAsString();
            } else {
              throw new AssertionError();
            }
        } else if ( keyElement.isJsonNull() ) {
            return "null";
        }

        throw new IllegalStateException( "Unable to serialize Map with complex keys that do not serialize as JsonPrimitive" );
    }

    private Object getOutputObject( final Object item ) {
        if ( shouldReplaceWithDynamicObject( item ) ) {
            return DynamicObject.create( item );
        }

        return item;
    }

    private boolean shouldReplaceWithDynamicObject( final Object item ) {
        return item instanceof Map || item instanceof Iterable;
    }

    private JsonElement writeArray( final Iterable<Object> iterable ) {
        final List<Object> outputIterable = makeOutputIterable( iterable );
        return gson.toJsonTree( outputIterable );
    }

    private List<Object> makeOutputIterable( final Iterable<Object> iterable ) {
        final List<Object> output = new ArrayList<Object>();
        for( final Object item : iterable ) {
            output.add( getOutputObject( item ) );
        }

        return output;
    }
}