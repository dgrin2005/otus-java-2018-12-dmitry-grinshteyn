package ru.otus;

import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceImpl;
import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;

import java.util.Arrays;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {

        try (DBService dbService = new DBServiceImpl(Arrays.asList(
                AddressDataSet.class,
                PhoneDataSet.class,
                UserDataSet.class))){
            System.out.println("Creating users...");
            UserDataSet user1 = new UserDataSet("Ivan", 23,
                    new AddressDataSet("Lenina"),
                    Arrays.asList(new PhoneDataSet("1111"), new PhoneDataSet("2222")));
            UserDataSet user2 = new UserDataSet("Pyotr", 24,
                    new AddressDataSet("Kirova"),
                    Arrays.asList(new PhoneDataSet("3333"), new PhoneDataSet("4444")));
            UserDataSet user3 = new UserDataSet("Nikolay", 25,
                   new AddressDataSet("Truda"),
                    Arrays.asList(new PhoneDataSet("6666"), new PhoneDataSet("5555")));
            user1 = dbService.create(user1);
            System.out.println("new user1 = " + user1);
            user2 = dbService.create(user2);
            System.out.println("new user2 = " + user2);
            user3 = dbService.create(user3);
            System.out.println("new user3 = " + user3);
            System.out.println("---");
            System.out.println("all users: " + dbService.getAll(UserDataSet.class));
            System.out.println("---");
            System.out.println("Deleting user id = 2...");
            dbService.deleteById(2, UserDataSet.class);
            System.out.println("all users: " + dbService.getAll(UserDataSet.class));
            System.out.println("---");
            System.out.println("Updating user id = 1...");
            user1 = dbService.getById(1, UserDataSet.class);
            System.out.println("user1 = " + user1);
            if (user1 != null) {
                for (PhoneDataSet phone : user1.getPhones()) {
                    dbService.deleteById(phone.getId(), PhoneDataSet.class);
                }
                user1.setName("Maria");
                user1.setAddress(new AddressDataSet("Mendeleeva"));
                user1.setPhones(Arrays.asList(new PhoneDataSet("12345"), new PhoneDataSet("54321")));
                user1 = dbService.update(user1);
                System.out.println("updated user1 = " + user1);
            }
            System.out.println("---");
            System.out.println("all users: " + dbService.getAll(UserDataSet.class));
            System.out.println("---");
            System.out.println("Deleting all users...");
            dbService.deleteAll(UserDataSet.class);
            dbService.deleteAll(PhoneDataSet.class);
            dbService.deleteAll(AddressDataSet.class);
            System.out.println("all users: " + dbService.getAll(UserDataSet.class));
            System.out.println("all addresses: " + dbService.getAll(AddressDataSet.class));
            System.out.println("all phones: " + dbService.getAll(PhoneDataSet.class));
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        }
    }

}
