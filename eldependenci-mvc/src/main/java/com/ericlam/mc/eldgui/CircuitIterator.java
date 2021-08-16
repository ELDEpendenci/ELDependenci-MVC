package com.ericlam.mc.eldgui;

import java.util.Iterator;

public final class CircuitIterator<E> implements Iterator<E> {

    private final E[] elements;
    private int index = 0;

    public CircuitIterator(E[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return elements.length > 0;
    }

    @Override
    public E next() {
        if (index == elements.length){
            index = 0;
        }
        return elements[index++];
    }
}
