package ru.otus.DBWorker;

import ru.otus.DBService.DBService;
import ru.otus.DBWorker.Actions.DBServiceActionsParameters;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;
import ru.otus.workers.WorkerActions;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBServiceCallable implements Callable {

    private final static Logger logger = Logger.getLogger(DBServiceCallable.class.getName());
    private final DBService dbService;
    private final SocketMessageWorker client;
    private final WorkerActions dbServiceActions;

    public DBServiceCallable(DBService dbService,
                             SocketMessageWorker client,
                             WorkerActions dbServiceActions) {
        this.dbService = dbService;
        this.client = client;
        this.dbServiceActions = dbServiceActions;
    }

    @Override
    public Object call() throws Exception {
        while (true){
            Message msg = client.take();
            logger.log(Level.INFO, "DB Message received: " + msg.toString());
            dbServiceActions.getActionMap().get(msg.getMessageId()).accept(
                        new DBServiceActionsParameters(msg, dbService, client));
        }
    }
}