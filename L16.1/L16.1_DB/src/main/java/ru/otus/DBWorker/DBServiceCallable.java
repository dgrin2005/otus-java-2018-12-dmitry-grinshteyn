package ru.otus.DBWorker;

import ru.otus.DBService.DBService;
import ru.otus.DBWorker.Actions.DBServiceActions;
import ru.otus.DBWorker.Actions.DBServiceActionsParameters;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DBServiceCallable implements Callable {

    private final static Logger logger = Logger.getLogger(DBServiceCallable.class.getName());
    private FileHandler fh;
    private final DBService dbService;
    private final SocketMessageWorker client;

    public DBServiceCallable(DBService dbService, SocketMessageWorker client) throws IOException {
        fh = new FileHandler("C:/MyDocs/DBService.log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        this.dbService = dbService;
        this.client = client;
    }

    @Override
    public Object call() throws Exception {
        while (true){
            Message msg = client.take();
            //if (msg.getTo().getId().equals(dbAddress.getId())) {
                logger.log(Level.INFO, "DB Message received: " + msg.toString());
                DBServiceActions.actionMap.get(msg.getMessageId()).accept(
                        new DBServiceActionsParameters(msg, dbService, client));
            //}
        }
    }
}