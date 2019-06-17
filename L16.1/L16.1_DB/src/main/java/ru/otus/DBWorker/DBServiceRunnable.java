package ru.otus.dbworker;

import ru.otus.dbservice.DBService;
import ru.otus.dbworker.actions.DBServiceActionsParameters;
import ru.otus.exception.MyMSException;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;
import ru.otus.workers.WorkerActions;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBServiceRunnable implements Runnable {

    private final static Logger logger = Logger.getLogger(DBServiceRunnable.class.getName());
    private final DBService dbService;
    private final SocketMessageWorker client;
    private final WorkerActions dbServiceActions;

    public DBServiceRunnable(DBService dbService,
                             SocketMessageWorker client,
                             WorkerActions dbServiceActions) {
        this.dbService = dbService;
        this.client = client;
        this.dbServiceActions = dbServiceActions;
    }

    @Override
    public void run() {
        while (true){
            try {
                Message msg = client.take();
                logger.log(Level.INFO, "DB Message received: " + msg.toString());
                Optional<Consumer> optionalAction = dbServiceActions.getAction(msg);
                Consumer action = optionalAction.orElseThrow(() ->
                        new MyMSException("Action not found for message " + msg));
                action.accept(new DBServiceActionsParameters(msg, dbService, client));
            } catch (MyMSException e) {
                e.printStackTrace();
            }
        }
    }
}