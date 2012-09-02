package com.spoon16.gson_clj;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Type;  

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;
import clojure.lang.Keyword;

public class DynamicObjectDeserializer {
    private final Gson gson;
    private final boolean keywordizeAttributeNames;

    public static DynamicObject deserialize( final Gson gson, final boolean keywordizeAttributeNames, final JsonElement jsonSource ) {
        return new DynamicObjectDeserializer( gson, keywordizeAttributeNames ).deserialize( jsonSource );
    }

    private DynamicObjectDeserializer( final Gson gson, final boolean keywordizeAttributeNames ) {
        this.gson = gson;
        this.keywordizeAttributeNames = keywordizeAttributeNames;
    }

    public DynamicObject deserialize( final JsonElement jsonSource ) {
        final Object result = readSource( jsonSource);
        return DynamicObject.create( result );
    }

    private Object readSource( final JsonElement jsonSource ) {
        if ( jsonSource.isJsonObject() ) {
            return readObject( jsonSource.getAsJsonObject() );
        } else if ( jsonSource.isJsonArray() ) {
            return readArray( jsonSource.getAsJsonArray() );
        }

        return gson.fromJson( jsonSource, Object.class );
    }

    protected IPersistentMap readObject( final JsonObject jsonSource ) {
        final Map<Object, Object> attributes = new HashMap<Object, Object>();
        readObjectAttributes( jsonSource, attributes );
        return PersistentHashMap.create( attributes );
    }

    private void readObjectAttributes( final JsonObject jsonSource, final Map<Object, Object> attributes ) {
        for ( final Map.Entry<String, JsonElement> attributeKeyValue : jsonSource.entrySet() ) {
            final String attributeName = attributeKeyValue.getKey();
            final Object attributeKey = keywordizeAttributeNames ? keywordizeAttributeName( attributeName ) : attributeName;

            final JsonElement attributeValue = attributeKeyValue.getValue();

            attributes.put( attributeKey, readSource( attributeValue ) );
        }
    }

    private Keyword keywordizeAttributeName( final String attributeName ) {
        return Keyword.intern( attributeName );
    }

    protected IPersistentVector readArray( final JsonArray jsonSource ) {
        final List<Object> items = new ArrayList<Object>();
        readArrayItems( jsonSource, items );
        return PersistentVector.create( items );
    }

    private void readArrayItems( final JsonArray jsonSource, final List<Object> items ) {
        for ( final JsonElement jsonArrayItemSource : jsonSource ) {
            items.add( readSource( jsonArrayItemSource ) );
        }
    }
}