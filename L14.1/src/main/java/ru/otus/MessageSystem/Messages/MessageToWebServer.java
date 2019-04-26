package ru.otus.MessageSystem.Messages;

import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Addressee;
import ru.otus.MessageSystem.Message;
import ru.otus.WebServer.WebServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MessageToWebServer extends Message {

    private final static Logger logger = Logger.getLogger(MessageToWebServer.class.getName());

    MessageToWebServer(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) throws IOException {
        if (addressee instanceof WebServer) {
            exec((WebServer) addressee);
        } else {
            logger.log(Level.INFO, "Wrong addressee (" + addressee + ") in messages for WebServer.");
        }
    }

    public abstract void exec(WebServer webServer) throws IOException;
}
