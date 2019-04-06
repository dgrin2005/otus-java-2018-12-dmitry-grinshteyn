package ru.otus.WebServer;

import ru.otus.DBService.DBService;

import javax.servlet.ServletException;
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

    UserDataSetServlet(DBService dbService) throws IOException {
        this.templateProcessor = new TemplateProcessor();
        this.dbService = dbService;
        this.errorMessage = "";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> pageVariables = preparePageVariables(req.getParameterMap(), dbService, errorMessage);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
        errorMessage = "";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        errorMessage = createNewUser(req.getParameterMap(), dbService);
        doGet(req, resp);
    }

    /*private Map<String, Object> preparePageVariables(Map<String, String[]> parameterMap) {
        String action = getParameterFromMap(parameterMap, "action");
        String userId = getParameterFromMap(parameterMap, "id");
        Map<String, Object> pageVariables = new HashMap<>();
        List<UserDataSetDto> userList = new ArrayList<>();
        long userAmount = 0;
        String userById = "";
        long id = -1;
            try {
                if (!userId.isEmpty()) {
                    id = Long.valueOf(userId);
                }
                userList = dbService.getAll(UserDataSet.class).stream()
                        .map(UserDataSet::from)
                        .collect(Collectors.toList());
                userAmount = dbService.count(UserDataSet.class);
                if (!action.isEmpty()) {
                    if (action.equals("delete")) {
                        dbService.deleteById(id, UserDataSet.class);
                        userList = dbService.getAll(UserDataSet.class).stream()
                                .map(UserDataSet::from)
                                .collect(Collectors.toList());
                        userAmount = dbService.count(UserDataSet.class);
                        id = -1;
                    }
                } else {
                    if (id != -1) {
                        userById = dbService.getById(id, UserDataSet.class).from().toString();
                    }
                }
            } catch (Exception e) {
                userAmount = userList.size();
                userById = "";
                errorMessage = e.getMessage();
                id = -1;
            }
        pageVariables.put("users", userList);
        pageVariables.put("useramount", userAmount);
        pageVariables.put("userid", id);
        pageVariables.put("userbyid", userById);
        pageVariables.put("errormessage", errorMessage);
        return pageVariables;
    }

    private void createNewUser(Map<String, String[]> parameterMap) {
        String name = getParameterFromMap(parameterMap, "name");
        String age = getParameterFromMap(parameterMap, "age");
        String address = getParameterFromMap(parameterMap, "address");
        String phone = getParameterFromMap(parameterMap, "phone");
        errorMessage = "";
        if(!name.isEmpty() && !age.isEmpty()) {
            try {
                if (!address.isEmpty()) {
                    if (!phone.isEmpty()) {
                        dbService.create(new UserDataSet(name, Integer.valueOf(age),
                                new AddressDataSet(address), Collections.singletonList(new PhoneDataSet(phone))));
                    } else {
                        dbService.create(new UserDataSet(name, Integer.valueOf(age),
                                new AddressDataSet(address), new ArrayList<>()));
                    }
                } else {
                    dbService.create(new UserDataSet(name, Integer.valueOf(age)));
                }
            } catch (MyOrmException e) {
                errorMessage = e.getMessage();
            }
        } else {
            errorMessage = "Не все реквизиты заполнены";
        }
    }

    private String getParameterFromMap(Map<String, String[]> parameterMap, String key) {
        if (parameterMap.get(key) != null) {
            return parameterMap.get(key)[0] != null ? parameterMap.get(key)[0] : "";
        } else {
            return "";
        }
    }*/
}
