package com.spoon16.gson_clj;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import clojure.lang.IPersistentVector;
import clojure.lang.PersistentVector;
import clojure.lang.IPersistentMap;
import clojure.lang.PersistentHashMap;
import clojure.lang.Keyword;

public class DefaultTypeAdapter extends TypeAdapter<DefaultType> {
	private final Gson gson;
	private final boolean keywordizeAttributeNames;

	private DefaultTypeAdapter( final Gson gson, final boolean keywordizeAttributeNames ) {
		this.gson = gson;
		this.keywordizeAttributeNames = keywordizeAttributeNames;
	}

	@Override
	public DefaultType read( final JsonReader in ) throws IOException {
		final Object result = readValue( in );
		return DefaultType.create( result );
	}

	private Object readValue( final JsonReader in ) throws IOException {
		final JsonToken token = in.peek();
		switch( token ) {
			case BEGIN_ARRAY:
				return readJsonArray( in );

			case BEGIN_OBJECT:
				return readJsonObject( in );

			case STRING:
				return readJsonPrimitiveString( in );

			case NUMBER:
				return readJsonPrimitiveNumber( in );

			case BOOLEAN:
				return readJsonPrimitiveBoolean( in );

			case NULL:
				readJsonNull( in );
				return null;
		}

		throw new IllegalStateException( "JsonToken not recognized, this should not happen" );
	}

	protected IPersistentVector readJsonArray( final JsonReader in ) throws IOException {
		final List<Object> items = new ArrayList<Object>();
		readJsonArrayItems( in, items );
		return PersistentVector.create( items );
	}

	private void readJsonArrayItems( final JsonReader in, final List<Object> items ) throws IOException {
		in.beginArray();
		while ( in.hasNext() ) {
			items.add( readValue( in ) );
		}
		in.endArray();
	}

	protected IPersistentMap readJsonObject( final JsonReader in ) throws IOException {
		final Map<Object, Object> attributes = new HashMap<Object, Object>();
		readJsonObjectAttributes( in, attributes );
		return PersistentHashMap.create( attributes );
	}

	private void readJsonObjectAttributes( final JsonReader in, final Map<Object, Object> attributes ) throws IOException {
		in.beginObject();
		while( in.hasNext() ) {
			final String attributeName = in.nextName();
			final Object attributeKey = keywordizeAttributeNames ? keywordizeAttributeName( attributeName ) : attributeName;
			attributes.put( attributeKey, readValue( in ) );
		}
		in.endObject();
	}

	private Keyword keywordizeAttributeName( final String attributeName ) {
		return Keyword.intern( attributeName );
	}

	protected String readJsonPrimitiveString( final JsonReader in ) throws IOException {
		return in.nextString();
	}

	protected Number readJsonPrimitiveNumber( final JsonReader in ) throws IOException {
		return in.nextDouble();
	}

	protected boolean readJsonPrimitiveBoolean( final JsonReader in ) throws IOException {
		return in.nextBoolean();
	}

	protected void readJsonNull( final JsonReader in ) throws IOException {
		in.nextNull();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write( final JsonWriter out, final DefaultType wrappedValue ) throws IOException {
		if ( wrappedValue == null ) {
			writeJsonNull( out );
			return;
		}

		final Object value = wrappedValue.getValue();
		if ( value == null ) {
			writeJsonNull( out );
			return;
		}

		final TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) gson.getAdapter( value.getClass() );
    	typeAdapter.write( out, value );
	}

	protected void writeJsonNull( final JsonWriter out ) throws IOException {
		out.nullValue();
	}

	public static class Factory implements TypeAdapterFactory {
		private final boolean keywordizeAttributeNames;

		public Factory( final boolean keywordizeAttributeNames ) {
			this.keywordizeAttributeNames = keywordizeAttributeNames;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> TypeAdapter<T> create( final Gson gson, final TypeToken<T> typeToken ) {
			if ( typeToken.getRawType() == DefaultType.class ) {
				return (TypeAdapter<T>) new DefaultTypeAdapter( gson, keywordizeAttributeNames );
			}

			return null;
		}
	}
}