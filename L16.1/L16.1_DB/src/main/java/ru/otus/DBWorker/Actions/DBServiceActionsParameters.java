package ru.otus.dbworker.actions;

import ru.otus.dbservice.DBService;
import ru.otus.messages.Message;
import ru.otus.workers.ActionsParameters;
import ru.otus.workers.SocketMessageWorker;

public class DBServiceActionsParameters extends ActionsParameters {

    private final DBService dbService;
    private final SocketMessageWorker client;

    public DBServiceActionsParameters(Message msg, DBService dbService, SocketMessageWorker client) {
        super(msg);
        this.dbService = dbService;
        this.client = client;
    }

    DBService getDbService() {
        return dbService;
    }

    SocketMessageWorker getClient() {
        return client;
    }
}
