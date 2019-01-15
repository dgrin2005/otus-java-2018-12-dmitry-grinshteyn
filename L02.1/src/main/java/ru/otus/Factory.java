package ru.otus;

import java.util.function.Supplier;

class Factory<T> {

    private final Supplier<T> supplier;

    Factory(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T createContents() {
        return supplier.get();
    }

    public Class<? extends Supplier> getFactoryClass(){
        return supplier.getClass();
    }

}
