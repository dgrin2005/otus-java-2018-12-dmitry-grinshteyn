package ru.otus.FEWorker.Actions;

import ru.otus.FrontEnd.FrontEndService;
import ru.otus.messages.Message;

import java.util.function.Consumer;

public class ActionShowPage implements Consumer<FEServiceActionsParameters> {

    @Override
    public void accept(FEServiceActionsParameters feServiceActionsParameters) {
        FrontEndService frontEndService = feServiceActionsParameters.getFrontEndService();
        Message msg = feServiceActionsParameters.getMsg();
        frontEndService.queryPut(msg.getUuid(), msg);
    }
}
