package ru.otus.FEWorker;

import ru.otus.FEWorker.Actions.FEServiceActionsParameters;
import ru.otus.FrontEnd.FrontEndService;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;
import ru.otus.workers.WorkerActions;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FEServiceCallable implements Callable {

    private final static Logger logger = Logger.getLogger(FrontEndService.class.getName());
    private final FrontEndService frontEndService;
    private final SocketMessageWorker client;
    private final WorkerActions feServiceActions;

    public FEServiceCallable(FrontEndService frontEndService,
                             SocketMessageWorker client,
                             WorkerActions feServiceActions) {
        this.frontEndService = frontEndService;
        this.client = client;
        this.feServiceActions = feServiceActions;
    }

    @Override
    public Object call() throws Exception {
        while (true){
            Message msg = client.take();
            logger.log(Level.INFO, "FE Message received: " + msg.toString());
            feServiceActions.getActionMap().get(msg.getMessageId()).accept(
                    new FEServiceActionsParameters(msg, frontEndService));
        }
    }
}