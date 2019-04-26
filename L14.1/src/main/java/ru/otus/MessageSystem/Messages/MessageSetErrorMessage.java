package ru.otus.MessageSystem.Messages;

import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.UserDataSetServlet;
import ru.otus.WebServer.WebServer;

public class MessageSetErrorMessage extends MessageToWebServer {

    private String errorMessage;

    public MessageSetErrorMessage(Address from, Address to, String errorMessage) {
        super(from, to);
        this.errorMessage = errorMessage;
    }

    @Override
    public void exec(WebServer webServer) {
        ((UserDataSetServlet)webServer.getHttpServlet()).setErrorMessage(errorMessage);
    }
}
