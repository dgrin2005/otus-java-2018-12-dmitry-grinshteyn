package ru.otus;

import ru.otus.DBInitialization.DBHibernateInitializationServiceImpl;
import ru.otus.DBInitialization.DBInitializationService;
import ru.otus.DBService.DBService;
import ru.otus.DBService.DBServiceHibernateImpl;
import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.workers.SocketMessageWorker;
import ru.otus.DBWorker.*;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.ServerMain.*;

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

            new DBServiceMain().start(dbService, Integer.parseInt(args[1]));

            while (true) {
            }
        } catch (Exception e) {
            throw new MyOrmException(e.getMessage(), e);
        } finally {
        }
    }

    private void start(DBService dbService, int index) throws InterruptedException, IOException {
        client = new ClientSocketMessageWorker(HOST, PORT_MS, index);
        client.init();
        logger.log(Level.INFO, "Start DB client");
        ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Callable<Integer> callable = new DBServiceCallable(dbService, client);
        executorService.submit(callable);
        executorService.shutdown();
    }

    private void destroy() throws IOException {
        client.close();
    }

}
