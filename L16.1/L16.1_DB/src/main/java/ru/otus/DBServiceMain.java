package ru.otus;

import ru.otus.dbinitialization.DBHibernateInitializationServiceImpl;
import ru.otus.dbinitialization.DBInitializationService;
import ru.otus.dbservice.DBService;
import ru.otus.dbservice.DBServiceHibernateImpl;
import ru.otus.dbworker.actions.ActionCreateNewUser;
import ru.otus.dbworker.actions.ActionDeleteUser;
import ru.otus.dbworker.actions.ActionFindUser;
import ru.otus.dbworker.actions.ActionGetUserList;
import ru.otus.dataset.AddressDataSet;
import ru.otus.dataset.PhoneDataSet;
import ru.otus.dataset.UserDataSet;
import ru.otus.exception.MyOrmException;
import ru.otus.exception.MyMSException;
import ru.otus.workers.SocketMessageWorker;
import ru.otus.dbworker.*;
import ru.otus.workers.WorkerActions;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.ServerMain.*;
import static ru.otus.messages.Message.*;

public class DBServiceMain {

    private final static Logger logger = Logger.getLogger(DBServiceMain.class.getName());
    private SocketMessageWorker client;

    public static void main(String[] args) throws MyOrmException {

        try (DBService dbService = new DBServiceHibernateImpl(args[0],
                AddressDataSet.class,
                PhoneDataSet.class,
                UserDataSet.class)){

            DBInitializationService dbInitializationService = new DBHibernateInitializationServiceImpl(dbService);
            dbInitializationService.initData();

            new DBServiceMain().start(dbService);

            while (true) {
            }
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        } finally {
        }
    }

    private void start(DBService dbService) throws MyMSException {
        try {
            client = new ClientSocketMessageWorker(HOST, PORT_MS);
            client.init();
            logger.log(Level.INFO, "Start DB client");
            ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            WorkerActions dbServiceActions = new WorkerActions();
            dbServiceActions.addAction(MESSAGE_ID_USER_LIST, new ActionGetUserList());
            dbServiceActions.addAction(MESSAGE_ID_CREATE_NEW_USER, new ActionCreateNewUser());
            dbServiceActions.addAction(MESSAGE_ID_FIND_USER, new ActionFindUser());
            dbServiceActions.addAction(MESSAGE_ID_DELETE_USER, new ActionDeleteUser());
            Runnable runnable = new DBServiceRunnable(dbService, client, dbServiceActions);
            executorService.submit(runnable);
            executorService.shutdown();
        } catch (IOException e) {
            throw new MyMSException(e.getMessage(), e);
        }
    }

    private void destroy() throws MyMSException {
        client.close();
    }

}
