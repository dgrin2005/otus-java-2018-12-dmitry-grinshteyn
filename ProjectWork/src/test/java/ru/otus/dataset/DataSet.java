package ru.otus.dataset;

import org.bson.types.ObjectId;
import ru.otus.annotation.Id;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSet)) return false;
        DataSet dataSet = (DataSet) o;
        return Objects.equals(getId(), dataSet.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
