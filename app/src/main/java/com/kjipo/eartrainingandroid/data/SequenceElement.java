package com.kjipo.eartrainingandroid.data;


public abstract class SequenceElement {

    private final ElementType elementType;


    protected SequenceElement(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementType getElementType() {
        return elementType;
    }

}
