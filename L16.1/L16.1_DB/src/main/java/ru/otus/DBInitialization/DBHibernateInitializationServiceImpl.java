package ru.otus.dbinitialization;

import ru.otus.dbservice.DBService;
import ru.otus.dataset.AddressDataSet;
import ru.otus.dataset.DataSet;
import ru.otus.dataset.PhoneDataSet;
import ru.otus.dataset.UserDataSet;
import ru.otus.exception.MyOrmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHibernateInitializationServiceImpl implements DBInitializationService {

    private DBService dbService;
    private List<UserDataSet> userDataSetList;

    public DBHibernateInitializationServiceImpl(DBService dbService) {
        this.dbService = dbService;
        userDataSetList = new ArrayList<>();
    }

    @Override
    public void initData() {
        userDataSetList.add(new UserDataSet("Ivan", 23,
                new AddressDataSet("Lenina"),
                Arrays.asList(new PhoneDataSet("1111"), new PhoneDataSet("2222"))));
        userDataSetList.add(new UserDataSet("Pyotr", 24,
                new AddressDataSet("Kirova"),
                Arrays.asList(new PhoneDataSet("3333"), new PhoneDataSet("4444"))));
        userDataSetList.add(new UserDataSet("Nikolay", 25,
                new AddressDataSet("Truda"),
                Arrays.asList(new PhoneDataSet("6666"), new PhoneDataSet("5555"))));
        saveDataIntoDB();
    }

    private void createRecord(DataSet x) {
        try {
            dbService.create(x);
        } catch (MyOrmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDataIntoDB() {
        userDataSetList.forEach(this::createRecord);
    }
}
