package ru.otus.MessageSystem.Messages;

import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.UserDataSetServlet;
import ru.otus.WebServer.WebServer;

public class MessageSetUserFoundedById extends MessageToWebServer {

    private final String userFoundedById;

    public MessageSetUserFoundedById(Address from, Address to, String userFoundedById) {
        super(from, to);
        this.userFoundedById = userFoundedById;
    }

    @Override
    public void exec(WebServer webServer) {
        ((UserDataSetServlet)webServer.getHttpServlet()).setUserFoundedById(userFoundedById);
    }
}
