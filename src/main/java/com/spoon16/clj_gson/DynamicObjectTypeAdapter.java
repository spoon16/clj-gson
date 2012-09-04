package com.spoon16.clj_gson;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.reflect.TypeToken;

/**
 * Controls the deserialization of JSON into a Clojure friendly datastructure when no specific target type has been
 * defined.
 *
 * Return types:
 *    123  (double)
 *    "hi" (string)
 *    true (boolean)
 *    null(null)
 *    {}({@link clojure.lang.IPersistentMap})
 *    []({@link clojure.lang.IPersistentVector})
 *
 * Optionally supports converting JSON object attribute names into Clojure keywords via the keywordizeAttributeNames field.
 *
 * Why not just register a {@link com.google.gson.JsonSerializer} via {@link com.google.gson.GsonBuilder#registerTypeAdapter}?
 *
 * It's necessary to define a specific TypeAdapter derivative in this case because we check to see whether or not
 * 'clojure-type-adapters' are enabled on a {@link com.google.gson.Gson} instance by querying for the type adapter
 * registered for the {@link DynamicObject} type.  If we did not have a strongly typed {@link TypeAdapter}
 * implementation a {@link com.google.gson.TreeTypeAdapter} instance would be provided and it would not be easy to
 * know that it represented the actual type adapter associated with 'clojure-type-adapters' being enabled. 
 */
public class DynamicObjectTypeAdapter extends TypeAdapter<DynamicObject> {
	private final Gson gson;
	private final boolean deserializeMapKeysAsKeywords;
	private final boolean forceSerializeMapKeysWithGson;

	private DynamicObjectTypeAdapter( final Gson gson, final boolean deserializeMapKeysAsKeywords, final boolean forceSerializeMapKeysWithGson ) {
		this.gson = gson;
		this.deserializeMapKeysAsKeywords = deserializeMapKeysAsKeywords;
		this.forceSerializeMapKeysWithGson = forceSerializeMapKeysWithGson;
	}

	@Override
	public DynamicObject read( final JsonReader in ) throws IOException {
		final JsonElement inputElement = new JsonParser().parse( in );
	    if ( inputElement.isJsonNull() ) {
	      return DynamicObject.create( null );
	    }

	    return DynamicObjectDeserializer.deserialize( gson, deserializeMapKeysAsKeywords, inputElement );
	}

	@Override
	public void write( final JsonWriter out, final DynamicObject wrappedValue ) throws IOException {
		final Object outputValue = wrappedValue.getValue();
		final JsonElement outputElement = DynamicObjectSerializer.serialize( gson, forceSerializeMapKeysWithGson, outputValue );
		gson.toJson( outputElement, out );
	}

	public static class Factory implements TypeAdapterFactory {
		private final boolean deserializeMapKeysAsKeywords;
		private final boolean forceSerializeMapKeysWithGson;

		public Factory( final boolean deserializeMapKeysAsKeywords, final boolean forceSerializeMapKeysWithGson ) {
			this.deserializeMapKeysAsKeywords = deserializeMapKeysAsKeywords;
			this.forceSerializeMapKeysWithGson = forceSerializeMapKeysWithGson;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> TypeAdapter<T> create( final Gson gson, final TypeToken<T> typeToken ) {
			if ( typeToken.getRawType() == DynamicObject.class ) {
				return (TypeAdapter<T>) new DynamicObjectTypeAdapter( gson, deserializeMapKeysAsKeywords, forceSerializeMapKeysWithGson );
			}

			return null;
		}
	}
}