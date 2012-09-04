package com.spoon16.clj_gson;

/**
 * This is just an {@link Object} wrapper. Gson does not allow the registered type adapter for {@link Object}
 * to be overridden ({@link com.google.gson.internal.bind.ObjectTypeAdapter}) which means that deserialization
 * can not be controlled when the target type is not specific. DynamicObject was introduced to wrap a
 * {@link Object} value and enable full control of the Gson deserialization process via {@link DynamicObjectTypeAdapter}.
 */
public class DynamicObject {
    private final Object value;

    public static DynamicObject create( final Object value ) {
        return new DynamicObject( value );
    }

    private DynamicObject( final Object value ) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}