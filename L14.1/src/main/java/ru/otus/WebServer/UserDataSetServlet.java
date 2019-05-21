package ru.otus.WebServer;

import ru.otus.DataSet.UserDataSet;
import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Messages.WS.MessageShowPage;
import ru.otus.WebServer.Dto.UserDataSetDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.otus.FrontEndService.FrontEndService.*;
import static ru.otus.WebServer.WebServerUtilites.*;

public class UserDataSetServlet extends HttpServlet {

    private static final String PAGE_TEMPLATE = "userdataset.html";
    private final TemplateProcessor templateProcessor;
    private String errorMessage;
    private long userId;
    private String userFoundedById;
    private List<UserDataSetDto> userList;
    private final FrontEndService frontEndService;

    UserDataSetServlet(FrontEndService frontEndService) {
        this.templateProcessor = new TemplateProcessor();
        this.errorMessage = "";
        this.userFoundedById = "";
        this.userId = 0;
        this.userList = new ArrayList<>();
        this.frontEndService = frontEndService;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID uuid = UUID.randomUUID();
        userId = getUserIdFromRequest(req);
        doAction(getAction(req), uuid);
        Map<String, Object> pageVariables = pageVariablesForUsersList(userList, userId, userFoundedById, errorMessage);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
        setErrorMessage("");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID uuid = UUID.randomUUID();
        actionCreateNewUser(req, uuid);
        Map<String, Object> pageVariables = pageVariablesForUsersList(userList, userId, userFoundedById, errorMessage);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
        setErrorMessage("");
    }

    private void doAction(String action, UUID uuid) {
        setUserFoundedById("");
        if (!action.isEmpty() && userId > 0) {
            if (action.equals(PARAMETER_ACTION_VALUE_DELETE)) {
                actionDeleteUser(uuid);
            } else {
                setUserId(-1);
                actionUserList(uuid);
            }
        } else {
            if (userId > 0) {
                actionFindUser(uuid);
            } else {
                setUserId(-1);
                actionUserList(uuid);
            }
        }
    }

    private void actionCreateNewUser(HttpServletRequest req, UUID uuid) {
        setUserId(-1);
        UserDataSet userDataSet = getUserDataSetFromRequest(req);
        if (userDataSet != null) {
            MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_CREATE_NEW_USER, userDataSet, uuid);
            setUserList(message.getUserListDto());
            setErrorMessage(message.getErrorMessage());
        } else {
            setErrorMessage(ERROR_FIELDS_NOT_FILLED);
        }
    }

    private void actionDeleteUser(UUID uuid) {
        MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_DELETE_USER, userId, uuid);
        setUserId(-1);
        setUserList(message.getUserListDto());
        setErrorMessage(message.getErrorMessage());
    }

    private void actionFindUser(UUID uuid) {
        MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_FIND_USER, userId, uuid);
        setUserId(message.getUserId());
        setUserFoundedById(message.getUserFoundedById());
        setErrorMessage(message.getErrorMessage());
    }

    private void actionUserList(UUID uuid) {
        MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_USER_LIST, uuid);
        setUserList(message.getUserListDto());
        setErrorMessage(message.getErrorMessage());
    }

    private void setUserList(List<UserDataSetDto> userList) {
        this.userList = userList;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    private void setUserFoundedById(String userFoundedById) {
        this.userFoundedById = userFoundedById;
    }

}
