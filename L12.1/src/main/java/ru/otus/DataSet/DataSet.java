package ru.otus.DataSet;

import javax.persistence.*;

@MappedSuperclass
public abstract class DataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DataSet() {
    }

    public DataSet(long id) {
        this.id = id;
    }
}
