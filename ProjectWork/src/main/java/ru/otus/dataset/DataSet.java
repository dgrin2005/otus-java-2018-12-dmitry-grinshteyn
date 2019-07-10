package ru.otus.dataset;

import org.bson.types.ObjectId;
import ru.otus.annotation.Id;

public abstract class DataSet {
    
    @Id
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public DataSet() {
    }

    public DataSet(ObjectId id) {
        this.id = id;
    }
}
