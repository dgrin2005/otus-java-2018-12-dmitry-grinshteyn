package ru.otus.FEWorker;

import ru.otus.FEWorker.Actions.FEServiceActions;
import ru.otus.FEWorker.Actions.FEServiceActionsParameters;
import ru.otus.FrontEnd.FrontEndService;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.ServerMain.feAddress;

public class FEServiceCallable implements Callable {

    private final static Logger logger = Logger.getLogger(FrontEndService.class.getName());
    private final FrontEndService frontEndService;
    private final SocketMessageWorker client;

    public FEServiceCallable(FrontEndService frontEndService, SocketMessageWorker client) {
        this.frontEndService = frontEndService;
        this.client = client;
    }

    @Override
    public Object call() throws Exception {
        while (true){
            Message msg = client.take();
            if (msg.getTo().getId().equals(feAddress.getId())) {
                logger.log(Level.INFO, "FE Message received: " + msg.toString());
                FEServiceActions.actionMap.get(msg.getMessageId()).accept(
                        new FEServiceActionsParameters(msg, frontEndService));
            }
        }
    }
}