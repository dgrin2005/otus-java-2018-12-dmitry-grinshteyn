package ru.otus.dataset;

import org.bson.types.ObjectId;
import ru.otus.annotation.Document;

import java.util.Arrays;
import java.util.List;

@Document("pds")
public class PhoneDataSet extends DataSet {
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
}
