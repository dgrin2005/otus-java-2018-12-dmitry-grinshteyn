package ru.otus.WebServer;

import ru.otus.DataSet.AddressDataSet;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.WebServer.Dto.UserDataSetDto;

import javax.servlet.http.HttpServletRequest;
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
    final static String PARAMETER_ACTION_VALUE_DELETE = "delete";
    final static String ERROR_FIELDS_NOT_FILLED = "Не все реквизиты заполнены";

    static String getAction(HttpServletRequest req) {
        return getParameterFromMap(req.getParameterMap(), PARAMETER_KEY_ACTION);
    }

    static long getUserIdFromRequest(HttpServletRequest req) {
        String userId = getParameterFromMap(req.getParameterMap(), PARAMETER_KEY_ID);
        if (!userId.isEmpty()) {
            return Long.valueOf(userId);
        } else {
            return -1;
        }
    }

    static UserDataSet getUserDataSetFromRequest(HttpServletRequest req) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        String name = getParameterFromMap(parameterMap, PARAMETER_KEY_NAME);
        String age = getParameterFromMap(parameterMap, PARAMETER_KEY_AGE);
        String address = getParameterFromMap(parameterMap, PARAMETER_KEY_ADDRESS);
        String phone = getParameterFromMap(parameterMap, PARAMETER_KEY_PHONE);
        if(!name.isEmpty() && !age.isEmpty()) {
            if (!address.isEmpty()) {
                if (!phone.isEmpty()) {
                    return  new UserDataSet(name, Integer.valueOf(age),
                        new AddressDataSet(address), Collections.singletonList(new PhoneDataSet(phone)));
                } else {
                    return new UserDataSet(name, Integer.valueOf(age),
                        new AddressDataSet(address), new ArrayList<>());
                }
            } else {
                return new UserDataSet(name, Integer.valueOf(age));
            }
        } else {
            return null;
        }
    }

    static List<UserDataSetDto> userDataSetListToDtoList(List<UserDataSet> userDataSetList) {
        return userDataSetList.stream()
                .map(UserDataSet::from)
                .collect(Collectors.toList());
    }

    static String userDataSetToDto(UserDataSet userDataSet) {
        return userDataSet.from().toString();
    }

    static Map<String, Object> pageVariablesForUsersList(List<UserDataSetDto> userList,
                                                                long userId,
                                                                String userFoundedById,
                                                                String errorMessage) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("users", userList);
        pageVariables.put("useramount", userList.size());
        pageVariables.put("userid", userId);
        pageVariables.put("userbyid", userFoundedById);
        pageVariables.put("errormessage", errorMessage);
        return pageVariables;
    }

    private static String getParameterFromMap(Map<String, String[]> parameterMap, String key) {
        if (parameterMap.get(key) != null) {
            return parameterMap.get(key)[0] != null ? parameterMap.get(key)[0] : "";
        } else {
            return "";
        }
    }

}
