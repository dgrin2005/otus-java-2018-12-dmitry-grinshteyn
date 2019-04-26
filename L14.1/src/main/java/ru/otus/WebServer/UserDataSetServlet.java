package ru.otus.WebServer;

import ru.otus.DataSet.UserDataSet;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;
import ru.otus.MessageSystem.MessageSystemContext;
import ru.otus.MessageSystem.Messages.*;
import ru.otus.WebServer.Dto.UserDataSetDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.otus.WebServer.WebServerUtilites.*;

public class UserDataSetServlet extends HttpServlet {

    private static final String PAGE_TEMPLATE = "userdataset.html";
    private final TemplateProcessor templateProcessor;
    private String errorMessage;
    private long userId;
    private String userFoundedById;
    private List<UserDataSetDto> userList;
    private final MessageSystemContext messageSystemContext;
    private final Address address;
    private ConcurrentHashMap<HttpServletRequest, Boolean> flagsToWait;

    UserDataSetServlet(MessageSystemContext messageSystemContext, Address address) throws IOException {
        this.templateProcessor = new TemplateProcessor();
        this.errorMessage = "";
        this.userFoundedById = "";
        this.userId = 0;
        this.userList = new ArrayList<>();
        this.messageSystemContext = messageSystemContext;
        this.address = address;
        flagsToWait =  new ConcurrentHashMap<>();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        userId = getUserIdFromRequest(req);
        setFlagToWait(req, true);
        doAction(getAction(req), req);
        while (flagsToWait.get(req)) {
        }
        Map<String, Object> pageVariables = pageVariablesForUsersList(userList, userId, userFoundedById, errorMessage);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
        setErrorMessage("");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        actionCreateNewUser(req);
        doGet(req, resp);
    }

    private void doAction(String action, HttpServletRequest req) {
        setUserFoundedById("");
        if (!action.isEmpty() && userId > 0) {
            if (action.equals(PARAMETER_ACTION_VALUE_DELETE)) {
                actionDeleteUser(req);
            } else {
                setUserId(-1);
                actionUserList(req);
            }
        } else {
            if (userId > 0) {
                actionFindUser(req);
            } else {
                setUserId(-1);
                actionUserList(req);
            }
        }
    }

    private void actionCreateNewUser(HttpServletRequest req) {
        UserDataSet userDataSet = getUserDataSetFromRequest(req);
        if (userDataSet != null) {
            Message message = new MessageCreateNewUser(address, messageSystemContext.getDbAddress(), userDataSet);
            messageSystemContext.getMessageSystem().sendMessage(message);
        } else {
            setErrorMessage(ERROR_FIELDS_NOT_FILLED);
        }
    }

    private void actionDeleteUser(HttpServletRequest req) {
        Message message = new MessageDeleteById(address, messageSystemContext.getDbAddress(), req, userId);
        messageSystemContext.getMessageSystem().sendMessage(message);
    }

    private void actionFindUser(HttpServletRequest req) {
        Message message = new MessageFindUser(address, messageSystemContext.getDbAddress(), req, userId);
        messageSystemContext.getMessageSystem().sendMessage(message);
    }

    private void actionUserList(HttpServletRequest req) {
        Message message = new MessageUserList(address, messageSystemContext.getDbAddress(), req,
                errorMessage, userFoundedById, userId, address);
        messageSystemContext.getMessageSystem().sendMessage(message);
    }

    public void setFlagToWait(HttpServletRequest req, boolean flagToWait) {
        flagsToWait.put(req, flagToWait);
    }

    public void setUserList(List<UserDataSetDto> userList) {
        this.userList = userList;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserFoundedById(String userFoundedById) {
        this.userFoundedById = userFoundedById;
    }
}
