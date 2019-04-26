package ru.otus.MessageSystem.Messages;

import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.Dto.UserDataSetDto;
import ru.otus.WebServer.UserDataSetServlet;
import ru.otus.WebServer.WebServer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MessageShowPage extends MessageToWebServer {

    private List<UserDataSetDto> userListDto;
    private String errorMessage;
    private String userFoundedById;
    private long userId;
    private HttpServletRequest httpServletRequest;

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
