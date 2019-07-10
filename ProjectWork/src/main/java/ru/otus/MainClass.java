package ru.otus;

import ru.otus.dao.DataSetDAO;
import ru.otus.dao.DataSetDAOImpl;
import ru.otus.dataset.AddressDataSet;
import ru.otus.dataset.PhoneDataSet;
import ru.otus.dataset.UserDataSet;
import ru.otus.exception.MyOrmException;

import java.util.Arrays;

public class MainClass {
    public static void main(String[] args) throws Exception {
        System.out.println("MONGODB ODM TEST");
        try (final DataSetDAO dao = new DataSetDAOImpl()){
            workWithDB(dao);
        }
        System.out.println("---------");
        System.out.println();
    }

    private static void workWithDB(DataSetDAO dbService) throws MyOrmException {
        AddressDataSet address1 = new AddressDataSet("Lenina");
        AddressDataSet address2 = new AddressDataSet("Kirova");
        AddressDataSet address3 = new AddressDataSet("Truda");
        AddressDataSet address4 = new AddressDataSet("Mendeleeva");

        System.out.println("Creating addresses...");
        address1 = dbService.create(address1);
        address2 = dbService.create(address2);
        address3 = dbService.create(address3);
        address4 = dbService.create(address4);
        System.out.println("all addresses: " + dbService.getAll(AddressDataSet.class));

        PhoneDataSet testPhone = new PhoneDataSet("3333");
        dbService.create(testPhone);

        UserDataSet user1 = new UserDataSet("Ivan", 23,
                address1,
                Arrays.asList(new PhoneDataSet("1111", "2222")));
        UserDataSet user2 = new UserDataSet("Pyotr", 24,
                address2,
                Arrays.asList(new PhoneDataSet("3333", "4444")));
        UserDataSet user3 = new UserDataSet("Nikolay", 25,
                address3,
                Arrays.asList(new PhoneDataSet("6666", "5555"), testPhone));
        System.out.println("Creating users...");
        user1 = dbService.create(user1);
        System.out.println("new user1 = " + user1);
        user2 = dbService.create(user2);
        System.out.println("new user2 = " + user2);
        user3 = dbService.create(user3);
        System.out.println("new user3 = " + user3);
        System.out.println("---");
        System.out.println("all users: " + dbService.getAll(UserDataSet.class));
        System.out.println("---");
        System.out.println("Deleting user name = 'Pyotr' ...");
        dbService.deleteByName("Pyotr", UserDataSet.class);
        System.out.println("all users: " + dbService.getAll(UserDataSet.class));
        System.out.println("---");
        System.out.println("Updating user name = 'Ivan' ...");
        user1 = dbService.getByName("Ivan", UserDataSet.class);
        System.out.println("user1 = " + user1);
        if (user1 != null) {
            user1.setName("Maria");
            user1.setAddress(address4);
            user1.setPhones(Arrays.asList(new PhoneDataSet("12345"), new PhoneDataSet("54321")));
            user1 = dbService.update(user1);
            System.out.println("updated user1 = " + user1);
        }
        System.out.println("---");
        System.out.println("all users: " + dbService.getAll(UserDataSet.class));
        System.out.println("---");
        System.out.println("all addresses: " + dbService.getAll(AddressDataSet.class));
    }

}
