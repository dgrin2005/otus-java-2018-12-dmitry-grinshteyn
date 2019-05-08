package ru.otus.WebServer;

import ru.otus.DataSet.UserDataSet;
import ru.otus.FrontEndService.FrontEndService;
import ru.otus.WebServer.Dto.UserDataSetDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        userId = getUserIdFromRequest(req);
        doAction(getAction(req));
        frontEndService.queryTake();
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

    private void doAction(String action) {
        setUserFoundedById("");
        if (!action.isEmpty() && userId > 0) {
            if (action.equals(PARAMETER_ACTION_VALUE_DELETE)) {
                actionDeleteUser();
            } else {
                setUserId(-1);
                actionUserList();
            }
        } else {
            if (userId > 0) {
                actionFindUser();
            } else {
                setUserId(-1);
                actionUserList();
            }
        }
    }

    public void actionCreateNewUser(HttpServletRequest req) {
        UserDataSet userDataSet = getUserDataSetFromRequest(req);
        if (userDataSet != null) {
            frontEndService.sendMessage(MESSAGE_ID_CREATE_NEW_USER, userDataSet, this);
        } else {
            setErrorMessage(ERROR_FIELDS_NOT_FILLED);
        }
    }

    private void actionDeleteUser() {
        frontEndService.sendMessage(MESSAGE_ID_DELETE_USER, errorMessage, userFoundedById, userId, this);
    }

    private void actionFindUser() {
        frontEndService.sendMessage(MESSAGE_ID_FIND_USER,  errorMessage, userFoundedById, userId, this);
    }

    private void actionUserList() {
        frontEndService.sendMessage(MESSAGE_ID_USER_LIST, errorMessage, userFoundedById, userId, this);
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
