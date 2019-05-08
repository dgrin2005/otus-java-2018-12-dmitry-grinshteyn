package ru.otus.MessageSystem.Messages.WS;

import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Addressee;
import ru.otus.MessageSystem.Message;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MessageToFrontEnd extends Message {

    private final static Logger logger = Logger.getLogger(MessageToFrontEnd.class.getName());

    MessageToFrontEnd(Address from, Address to, String messsageId) {
        super(from, to, messsageId);
    }

    @Override
    public void exec(Addressee addressee) throws IOException {
        if (addressee instanceof FrontEndService) {
            exec((FrontEndService) addressee);
        } else {
            logger.log(Level.INFO, "Wrong addressee (" + addressee + ") in messages for WebServer.");
        }
    }

    public abstract void exec(FrontEndService frontEndService) throws IOException;
}
