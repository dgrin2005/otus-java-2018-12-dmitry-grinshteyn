package ru.otus;

import ru.otus.DBInitialization.DBHibernateInitializationServiceImpl;
import ru.otus.DBInitialization.DBInitializationService;
import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceHibernateImpl;
import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.WebServer.WebServer;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {
        try (DBService dbService = new DBServiceHibernateImpl("hibernate.cfg.xml",
                AddressDataSet.class,
                PhoneDataSet.class,
                UserDataSet.class)){
            DBInitializationService dbInitializationService = new DBHibernateInitializationServiceImpl(dbService);
            dbInitializationService.initData();
            dbInitializationService.saveDataIntoDB();
            WebServer webServer = new WebServer(dbService);
            webServer.start();
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        }
    }
}
