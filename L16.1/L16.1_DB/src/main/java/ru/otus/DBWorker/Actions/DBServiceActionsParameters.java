package ru.otus.DBWorker.Actions;

import ru.otus.DBService.DBService;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;

public class DBServiceActionsParameters {

    private final Message msg;
    private final DBService dbService;
    private final SocketMessageWorker client;

    public DBServiceActionsParameters(Message msg, DBService dbService, SocketMessageWorker client) {
        this.msg = msg;
        this.dbService = dbService;
        this.client = client;
    }

    public Message getMsg() {
        return msg;
    }

    public DBService getDbService() {
        return dbService;
    }

    public SocketMessageWorker getClient() {
        return client;
    }
}
