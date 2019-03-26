package ru.otus;

import ru.otus.DAO.DataSetDAO;
import ru.otus.DAO.DataSetDAOImpl;
import ru.otus.DataSet.UserDataSet;

import java.sql.Connection;
import java.sql.SQLException;

public class MainClass {
    public static void main(String[] args) {
        try (final Connection connection = DBUtilites.getMySQLConnection()){
            DataSetDAO dao = new DataSetDAOImpl(connection);
            UserDataSet user1 = new UserDataSet("Ivan", 20);
            UserDataSet user2 = new UserDataSet("Pyotr", 23);
            UserDataSet user3 = new UserDataSet("Nikolay", 23);
            dao.create(user1);
            dao.create(user2);
            dao.create(user3);
            System.out.println(dao.getAll(UserDataSet.class));
            dao.deleteById(2);
            user1 = dao.getById(1, UserDataSet.class);
            System.out.println(dao.getById(1, UserDataSet.class));
            if (user1 != null) {
                user1.setName("Maria");
                dao.update(user1);
            }
            System.out.println(dao.getAll(UserDataSet.class));
            dao.deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
