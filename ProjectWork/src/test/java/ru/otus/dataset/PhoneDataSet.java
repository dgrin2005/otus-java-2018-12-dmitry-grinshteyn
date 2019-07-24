package ru.otus.dataset;

import org.bson.types.ObjectId;
import ru.otus.annotation.Field;
import ru.otus.annotation.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Document("pds")
public class PhoneDataSet extends DataSet {
    @Field("phones")
    private List<String> numbers;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String... numbers) {
        super(new ObjectId());
        this.numbers = Arrays.asList(numbers);
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(String... numbers) {
        this.numbers = Arrays.asList(numbers);
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + getId() +
                ", numbers='" + numbers + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        if (!super.equals(o)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return Objects.equals(getNumbers(), that.getNumbers());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getNumbers());
    }
}
