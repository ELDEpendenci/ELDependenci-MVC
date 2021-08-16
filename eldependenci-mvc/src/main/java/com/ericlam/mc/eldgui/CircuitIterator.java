package com.ericlam.mc.eldgui;

import java.util.Collection;
import java.util.ListIterator;

public final class CircuitIterator<E> implements ListIterator<E> {

    private final E[] elements;
    private int index = -1;

    public CircuitIterator(E[] elements) {
        this.elements = elements;
    }

    public CircuitIterator(E[] elements, int index) {
        this.elements = elements;
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public CircuitIterator(Collection<E> elements) {
        this.elements = (E[]) elements.toArray();
    }

    @SuppressWarnings("unchecked")
    public CircuitIterator(Collection<E> elements, int index) {
        this.elements = (E[]) elements.toArray();
        this.index = index;
    }

    @Override
    public boolean hasNext() {
        return elements.length > 0;
    }

    @Override
    public E next() {
        if (index == elements.length - 1) {
            index = -1;
        }
        return elements[++index];
    }

    @Override
    public boolean hasPrevious() {
        return elements.length > 0;
    }

    @Override
    public E previous() {
        if (index <= 0) {
            index = elements.length;
        }
        return elements[--index];
    }

    @Override
    public int nextIndex() {
        return index + 1;
    }

    @Override
    public int previousIndex() {
        return index - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

    @Override
    public void set(E e) {
        throw new UnsupportedOperationException("set");
    }

    @Override
    public void add(E e) {
        throw new UnsupportedOperationException("add");
    }
}
