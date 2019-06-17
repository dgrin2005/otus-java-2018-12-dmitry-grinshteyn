package ru.otus.feworker.actions;

import ru.otus.frontend.FrontEndService;
import ru.otus.messages.Message;
import ru.otus.workers.ActionsParameters;

public class FEServiceActionsParameters extends ActionsParameters {

    private final FrontEndService frontEndService;

    public FEServiceActionsParameters(Message msg, FrontEndService frontEndService) {
        super(msg);
        this.frontEndService = frontEndService;
    }

    FrontEndService getFrontEndService() {
        return frontEndService;
    }
}
