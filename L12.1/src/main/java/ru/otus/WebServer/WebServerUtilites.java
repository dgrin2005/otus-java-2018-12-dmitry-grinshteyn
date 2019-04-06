package ru.otus.WebServer;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.WebServer.Dto.UserDataSetDto;

import java.util.*;
import java.util.stream.Collectors;

class WebServerUtilites {

    final static String STATIC_PATH_RESOURCE = "/static";
    final static String TEMPLATE_PATH_RESOURCE = "/templates";
    final static int PORT = 8080;
    private final static String PARAMETER_KEY_ACTION = "action";
    private final static String PARAMETER_KEY_ID = "id";
    private final static String PARAMETER_KEY_NAME = "name";
    private final static String PARAMETER_KEY_AGE = "age";
    private final static String PARAMETER_KEY_ADDRESS = "address";
    private final static String PARAMETER_KEY_PHONE = "phone";
    private final static String PARAMETER_ACTION_VALUE_DELETE = "delete";
    private final static String ERROR_FIELDS_NOT_FILLED = "Не все реквизиты заполнены";

    static Map<String, Object> preparePageVariables(Map<String, String[]> parameterMap,
                                                     DBService dbService,
                                                     String errorMessage) {
        String action = getParameterFromMap(parameterMap, PARAMETER_KEY_ACTION);
        String userId = getParameterFromMap(parameterMap, PARAMETER_KEY_ID);
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
                if (action.equals(PARAMETER_ACTION_VALUE_DELETE)) {
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

    static String createNewUser(Map<String, String[]> parameterMap,
                               DBService dbService) {
        String name = getParameterFromMap(parameterMap, PARAMETER_KEY_NAME);
        String age = getParameterFromMap(parameterMap, PARAMETER_KEY_AGE);
        String address = getParameterFromMap(parameterMap, PARAMETER_KEY_ADDRESS);
        String phone = getParameterFromMap(parameterMap, PARAMETER_KEY_PHONE);
        String errorMessage = "";
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
            errorMessage = ERROR_FIELDS_NOT_FILLED;
        }
        return errorMessage;
    }

    private static String getParameterFromMap(Map<String, String[]> parameterMap, String key) {
        if (parameterMap.get(key) != null) {
            return parameterMap.get(key)[0] != null ? parameterMap.get(key)[0] : "";
        } else {
            return "";
        }
    }

}
