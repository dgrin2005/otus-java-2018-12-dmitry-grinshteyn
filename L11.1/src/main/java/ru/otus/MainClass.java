package ru.otus;

import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceHibernateImpl;
import ru.otus.DBService.DBServiceImpl;
import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;

import java.util.Arrays;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {

        System.out.println("MY ORM");
        try (DBService dbService = new DBServiceImpl()){
            workWithDB(dbService);
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        }
        System.out.println("---------");
        System.out.println();

        System.out.println("HIBERNATE");
        try (DBService dbService = new DBServiceHibernateImpl(Arrays.asList(
                AddressDataSet.class,
                PhoneDataSet.class,
                UserDataSet.class))){
            workWithDB(dbService);
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        }
        System.out.println("---------");
    }

    private static void workWithDB(DBService dbService) throws MyOrmException {
        AddressDataSet address1 = new AddressDataSet("Lenina");
        AddressDataSet address2 = new AddressDataSet("Kirova");
        AddressDataSet address3 = new AddressDataSet("Truda");
        AddressDataSet address4 = new AddressDataSet("Mendeleeva");
        if (dbService instanceof DBServiceImpl) {
            System.out.println("Creating addresses...");
            address1 = dbService.create(address1);
            address2 = dbService.create(address2);
            address3 = dbService.create(address3);
            address4 = dbService.create(address4);
            System.out.println("all addresses: " + dbService.getAll(AddressDataSet.class));
        }
        UserDataSet user1 = new UserDataSet("Ivan", 23,
                address1,
                Arrays.asList(new PhoneDataSet("1111"), new PhoneDataSet("2222")));
        UserDataSet user2 = new UserDataSet("Pyotr", 24,
                address2,
                Arrays.asList(new PhoneDataSet("3333"), new PhoneDataSet("4444")));
        UserDataSet user3 = new UserDataSet("Nikolay", 25,
                address3,
                Arrays.asList(new PhoneDataSet("6666"), new PhoneDataSet("5555")));
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
        System.out.println("Deleting user id = 2...");
        dbService.deleteById(2, UserDataSet.class);
        System.out.println("all users: " + dbService.getAll(UserDataSet.class));
        System.out.println("---");
        System.out.println("Updating user id = 1...");
        user1 = dbService.getById(1, UserDataSet.class);
        System.out.println("user1 = " + user1);
        if (user1 != null) {
            if (dbService instanceof DBServiceHibernateImpl) {
                for (PhoneDataSet phone : user1.getPhones()) {
                    dbService.deleteById(phone.getId(), PhoneDataSet.class);
                }
            }
            user1.setName("Maria");
            user1.setAddress(address4);
            user1.setPhones(Arrays.asList(new PhoneDataSet("12345"), new PhoneDataSet("54321")));
            user1 = dbService.update(user1);
            System.out.println("updated user1 = " + user1);
        }
        System.out.println("---");
        System.out.println("all users: " + dbService.getAll(UserDataSet.class));
        System.out.println("---");
        System.out.println("Deleting all users...");
        dbService.deleteAll(UserDataSet.class);
        System.out.println("all users: " + dbService.getAll(UserDataSet.class));
        dbService.deleteAll(AddressDataSet.class);
        System.out.println("all addresses: " + dbService.getAll(AddressDataSet.class));
        if (dbService instanceof DBServiceHibernateImpl) {
            dbService.deleteAll(PhoneDataSet.class);
            System.out.println("all phones: " + dbService.getAll(PhoneDataSet.class));
        }
    }

}
