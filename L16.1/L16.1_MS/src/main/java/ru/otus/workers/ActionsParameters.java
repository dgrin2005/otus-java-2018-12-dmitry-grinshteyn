package ru.otus.workers;

import ru.otus.messages.Message;

public class ActionsParameters {

    private final Message msg;

    public ActionsParameters(Message msg) {
        this.msg = msg;
    }

    public Message getMsg() {
        return msg;
    }

}
