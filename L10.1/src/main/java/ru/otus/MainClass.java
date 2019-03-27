package ru.otus;

import ru.otus.DAO.DataSetDAO;
import ru.otus.DAO.DataSetDAOImpl;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;

import java.sql.Connection;
import java.sql.SQLException;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {
        try (final Connection connection = DBUtilites.getMySQLConnection()){
            DataSetDAO dao = new DataSetDAOImpl(connection);
            UserDataSet user1 = new UserDataSet("Ivan", 20);
            UserDataSet user2 = new UserDataSet("Pyotr", 23);
            UserDataSet user3 = new UserDataSet("Nikolay", 23);
            user1 = dao.create(user1);
            System.out.println("new user1 = " + user1);
            user2 = dao.create(user2);
            System.out.println("new user2 = " + user2);
            user3 = dao.create(user3);
            System.out.println("new user3 = " + user3);
            System.out.println(dao.getAll(UserDataSet.class));
            dao.deleteById(2, UserDataSet.class);
            user1 = dao.getById(1, UserDataSet.class);
            System.out.println("user1 = " + user1);
            if (user1 != null) {
                user1.setName("Maria");
                user1 = dao.update(user1);
                System.out.println("updated user1 = " + user1);
            }
            System.out.println(dao.getAll(UserDataSet.class));
            dao.deleteAll(UserDataSet.class);
        } catch (SQLException e) {
            throw new MyOrmException(e);
        }
    }

}
