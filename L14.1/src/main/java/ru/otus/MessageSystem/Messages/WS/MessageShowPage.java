package ru.otus.MessageSystem.Messages.WS;

import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.Dto.UserDataSetDto;
import ru.otus.WebServer.UserDataSetServlet;
import ru.otus.WebServer.WebServer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MessageShowPage extends MessageToWebServer {

    private final List<UserDataSetDto> userListDto;
    private final String errorMessage;
    private final String userFoundedById;
    private final long userId;
    private final HttpServletRequest httpServletRequest;

    public MessageShowPage(Address from, Address to, HttpServletRequest httpServletRequest,
                           List<UserDataSetDto> userListDto, String errorMessage, String userFoundedById, long userId) {
        super(from, to);
        this.httpServletRequest = httpServletRequest;
        this.userListDto = userListDto;
        this.errorMessage = errorMessage;
        this.userFoundedById = userFoundedById;
        this.userId = userId;
    }

    @Override
    public void exec(WebServer webServer) {
        ((UserDataSetServlet)webServer.getHttpServlet()).setUserList(userListDto);
        ((UserDataSetServlet)webServer.getHttpServlet()).setErrorMessage(errorMessage);
        ((UserDataSetServlet)webServer.getHttpServlet()).setUserFoundedById(userFoundedById);
        ((UserDataSetServlet)webServer.getHttpServlet()).setUserId(userId);
        ((UserDataSetServlet)webServer.getHttpServlet()).setFlagToWait(httpServletRequest,false);
    }
}
