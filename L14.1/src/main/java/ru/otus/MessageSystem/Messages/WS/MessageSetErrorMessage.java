package ru.otus.MessageSystem.Messages.WS;

import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.UserDataSetServlet;
import ru.otus.WebServer.WebServer;

public class MessageSetErrorMessage extends MessageToWebServer {

    private final String errorMessage;

    public MessageSetErrorMessage(Address from, Address to, String errorMessage) {
        super(from, to);
        this.errorMessage = errorMessage;
    }

    @Override
    public void exec(WebServer webServer) {
        ((UserDataSetServlet)webServer.getHttpServlet()).setErrorMessage(errorMessage);
    }
}
