package ru.otus.MessageSystem.Messages.WS;

import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.UserDataSetServlet;

public class MessageSetErrorMessage extends MessageToFrontEnd {

    private final String errorMessage;
    private final UserDataSetServlet userDataSetServlet;

    public MessageSetErrorMessage(Address from, Address to, String errorMessage, UserDataSetServlet userDataSetServlet) {
        super(from, to, "");
        this.errorMessage = errorMessage;
        this.userDataSetServlet = userDataSetServlet;
    }

    @Override
    public void exec(FrontEndService frontEndService) {
        frontEndService.setErrorMessage(userDataSetServlet, errorMessage);
    }
}
