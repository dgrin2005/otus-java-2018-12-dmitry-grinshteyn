package ru.otus.FEWorker.Actions;

import ru.otus.FrontEnd.FrontEndService;
import ru.otus.messages.Message;

public class FEServiceActionsParameters {

    private final Message msg;
    private final FrontEndService frontEndService;

    public FEServiceActionsParameters(Message msg, FrontEndService frontEndService) {
        this.msg = msg;
        this.frontEndService = frontEndService;
    }

    Message getMsg() {
        return msg;
    }

    FrontEndService getFrontEndService() {
        return frontEndService;
    }
}
