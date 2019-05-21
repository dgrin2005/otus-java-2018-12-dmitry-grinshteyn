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
import ru.otus.FrontEndService.FrontEndService;
import ru.otus.WebServer.WebServer;

public class MainClass {
    public static void main(String[] args) throws MyOrmException {
        final MessageSystem messageSystem = new MessageSystem();
        final Address frontAddress = new Address("WS");
        final Address dbAddress = new Address("DB");
        final MessageSystemContext context = new MessageSystemContext(messageSystem, frontAddress, dbAddress);
        WebServer webServer = null;

        try (DBService dbService = new DBServiceHibernateImpl("hibernate.cfg.xml",
                context, dbAddress,
                AddressDataSet.class,
                PhoneDataSet.class,
                UserDataSet.class)){

            DBInitializationService dbInitializationService = new DBHibernateInitializationServiceImpl(dbService);
            dbInitializationService.initData();
            dbInitializationService.saveDataIntoDB();
            FrontEndService frontEndService = new FrontEndService(context, frontAddress);
            webServer = new WebServer(frontEndService);
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
