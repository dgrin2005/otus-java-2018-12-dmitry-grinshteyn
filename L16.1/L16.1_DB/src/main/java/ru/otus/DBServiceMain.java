package ru.otus;

import ru.otus.DBInitialization.DBHibernateInitializationServiceImpl;
import ru.otus.DBInitialization.DBInitializationService;
import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceHibernateImpl;
import ru.otus.DBWorker.Actions.ActionCreateNewUser;
import ru.otus.DBWorker.Actions.ActionDeleteUser;
import ru.otus.DBWorker.Actions.ActionFindUser;
import ru.otus.DBWorker.Actions.ActionGetUserList;
import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.exception.MyMSException;
import ru.otus.workers.SocketMessageWorker;
import ru.otus.DBWorker.*;
import ru.otus.workers.WorkerActions;

import java.io.IOException;
import java.util.concurrent.Callable;
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
            Callable<Integer> callable = new DBServiceCallable(dbService, client, dbServiceActions);
            executorService.submit(callable);
            executorService.shutdown();
        } catch (IOException e) {
            throw new MyMSException(e.getMessage(), e);
        }
    }

    private void destroy() throws MyMSException {
        client.close();
    }

}
