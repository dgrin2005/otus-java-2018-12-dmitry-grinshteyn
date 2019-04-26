package ru.otus;

import ru.otus.DBInitialization.DBHibernateInitializationServiceImpl;
import ru.otus.DBInitialization.DBInitializationService;
import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceHibernateImpl;
import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.MessageSystem;
import ru.otus.MessageSystem.MessageSystemContext;
import ru.otus.WebServer.WebServer;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {
        MessageSystem messageSystem = new MessageSystem();

        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("WS");
        context.setWebServerAddress(frontAddress);
        Address dbAddress = new Address("DB");
        context.setDbAddress(dbAddress);

        WebServer webServer = null;

        try (DBService dbService = new DBServiceHibernateImpl("hibernate.cfg.xml",
                context, dbAddress,
                AddressDataSet.class,
                PhoneDataSet.class,
                UserDataSet.class)){

            dbService.initInMessageSystem();

            DBInitializationService dbInitializationService = new DBHibernateInitializationServiceImpl(dbService);
            dbInitializationService.initData();
            dbInitializationService.saveDataIntoDB();
            webServer = new WebServer(context, frontAddress);
            webServer.initInMessageSystem();
            messageSystem.start();
            webServer.start();
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        } finally {
            messageSystem.stop();
            if (webServer != null) {
                webServer.stop();
            }
        }
    }
}
