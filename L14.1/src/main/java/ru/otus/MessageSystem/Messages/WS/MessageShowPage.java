package ru.otus.MessageSystem.Messages.WS;

import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.Dto.UserDataSetDto;
import ru.otus.WebServer.UserDataSetServlet;

import java.util.List;

public class MessageShowPage extends MessageToFrontEnd {

    private final List<UserDataSetDto> userListDto;
    private final String errorMessage;
    private final String userFoundedById;
    private final long userId;
    private final UserDataSetServlet userDataSetServlet;

    public MessageShowPage(Address from, Address to,
                           List<UserDataSetDto> userListDto, String errorMessage, String userFoundedById, long userId,
                           UserDataSetServlet userDataSetServlet) {
        super(from, to, "");
        this.userListDto = userListDto;
        this.errorMessage = errorMessage;
        this.userFoundedById = userFoundedById;
        this.userId = userId;
        this.userDataSetServlet = userDataSetServlet;
    }

    @Override
    public void exec(FrontEndService frontEndService) {
        frontEndService.queryPut();
        frontEndService.showPage(userDataSetServlet, userListDto, errorMessage, userFoundedById, userId);
    }
}
