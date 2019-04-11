package ru.otus.WebServer;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.WebServer.Dto.UserDataSetDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static ru.otus.WebServer.WebServerUtilites.*;

public class UserDataSetServlet extends HttpServlet {

    private static final String PAGE_TEMPLATE = "userdataset.html";
    private final DBService dbService;
    private final TemplateProcessor templateProcessor;
    private String errorMessage;
    private long userId;
    private String userFoundedById;
    private List<UserDataSetDto> userList;

    UserDataSetServlet(DBService dbService) throws IOException {
        this.templateProcessor = new TemplateProcessor();
        this.dbService = dbService;
        this.errorMessage = "";
        this.userFoundedById = "";
        this.userId = 0;
        this.userList = new ArrayList<>();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        userId = getUserIdFromRequest(req);
        doAction(getAction(req));
        Map<String, Object> pageVariables = pageVariablesForUsersList(userList, userId, userFoundedById, errorMessage);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
        errorMessage = "";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        actionCreateNewUser(req);
        doGet(req, resp);
    }

    private void doAction(String action) {
        userFoundedById = "";
        if (!action.isEmpty() && userId > 0) {
            if (action.equals(PARAMETER_ACTION_VALUE_DELETE)) {
                actionDeleteUser();
            }
        } else {
            if (userId > 0) {
                actionFindUser();
            } else {
                userId = -1;
            }
        }
        actionUserList();
    }

    private void actionCreateNewUser(HttpServletRequest req) {
        try {
            UserDataSet userDataSet = getUserDataSetFromRequest(req);
            if (userDataSet != null) {
                dbService.create(userDataSet);
            } else {
                errorMessage = ERROR_FIELDS_NOT_FILLED;
            }
        } catch (MyOrmException e) {
            errorMessage = e.getMessage();
        }
    }

    private void actionDeleteUser() {
        try {
            dbService.deleteById(userId, UserDataSet.class);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        userId = -1;
    }

    private void actionFindUser() {
        try {
            userFoundedById = userDataSetToDto(dbService.getById(userId, UserDataSet.class));
        } catch (Exception e) {
            errorMessage = e.getMessage();
            userId = -1;
        }
    }

    private void actionUserList() {
        userList = new ArrayList<>();
        try {
            userList = userDataSetListToDtoList(dbService.getAll(UserDataSet.class));
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

}
