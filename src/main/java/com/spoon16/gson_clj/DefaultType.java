package com.spoon16.gson_clj;

public class DefaultType {
    private final Object value;

    public static DefaultType create( final Object value ) {
        return new DefaultType( value );
    }

    private DefaultType( final Object value ) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}