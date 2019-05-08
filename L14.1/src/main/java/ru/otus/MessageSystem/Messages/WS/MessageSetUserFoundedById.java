package ru.otus.MessageSystem.Messages.WS;

import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.UserDataSetServlet;

public class MessageSetUserFoundedById extends MessageToFrontEnd {

    private final String userFoundedById;
    private final UserDataSetServlet userDataSetServlet;

    public MessageSetUserFoundedById(Address from, Address to, String userFoundedById, UserDataSetServlet userDataSetServlet) {
        super(from, to, "");
        this.userFoundedById = userFoundedById;
        this.userDataSetServlet = userDataSetServlet;
    }

    @Override
    public void exec(FrontEndService frontEndService) {
        frontEndService.setUserFoundedById(userDataSetServlet, userFoundedById);
    }
}
