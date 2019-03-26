package ru.otus.DataSet;

public abstract class DataSet {
    private long id;

    public long getId() {
        return id;
    }

    public DataSet() {
    }

    public DataSet(long id) {
        this.id = id;
    }
}
