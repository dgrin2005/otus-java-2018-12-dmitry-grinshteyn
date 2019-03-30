package ru.otus;

import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceImpl;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {

        try (DBService dbService = new DBServiceImpl(UserDataSet.class)){
            UserDataSet user1 = new UserDataSet("Ivan", 20);
            UserDataSet user2 = new UserDataSet("Pyotr", 23);
            UserDataSet user3 = new UserDataSet("Nikolay", 23);
            user1 = dbService.create(user1);
            System.out.println("new user1 = " + user1);
            user2 = dbService.create(user2);
            System.out.println("new user2 = " + user2);
            user3 = dbService.create(user3);
            System.out.println("new user3 = " + user3);
            System.out.println(dbService.getAll(UserDataSet.class));
            dbService.deleteById(2, UserDataSet.class);
            user1 = dbService.getById(1, UserDataSet.class);
            System.out.println("user1 = " + user1);
            if (user1 != null) {
                user1.setName("Maria");
                user1 = dbService.update(user1);
                System.out.println("updated user1 = " + user1);
            }
            System.out.println(dbService.getAll(UserDataSet.class));
            dbService.deleteAll(UserDataSet.class);
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        }
    }

}
