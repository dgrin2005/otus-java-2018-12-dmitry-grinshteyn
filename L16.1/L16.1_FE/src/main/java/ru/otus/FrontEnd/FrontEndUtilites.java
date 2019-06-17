package ru.otus.frontend;

import ru.otus.dataset.AddressDataSet;
import ru.otus.dataset.PhoneDataSet;
import ru.otus.dataset.UserDataSet;
import ru.otus.dataset.dto.UserDataSetDto;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class FrontEndUtilites {

    final static String TEMPLATE_PATH_RESOURCE = "/templates";
    private final static String PARAMETER_KEY_ACTION = "action";
    private final static String PARAMETER_KEY_ID = "id";
    private final static String PARAMETER_KEY_NAME = "name";
    private final static String PARAMETER_KEY_AGE = "age";
    private final static String PARAMETER_KEY_ADDRESS = "address";
    private final static String PARAMETER_KEY_PHONE = "phone";
    public final static String PARAMETER_ACTION_VALUE_DELETE = "delete";
    public final static String ERROR_FIELDS_NOT_FILLED = "Не все реквизиты заполнены";

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

    public static UserDataSet getUserDataSetFromRequest(HttpServletRequest req) {
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

    static Map<String, Object> pageVariablesForUsersList() {
        String errorMessage = "";
        long userId = 0;
        String userFoundedById = "";
        List<UserDataSetDto> userList =  new ArrayList<>();
        Map<String, Object> pageVariables = new HashMap<>();
        setUserListToMap(pageVariables, userList);
        setUserIdToMap(pageVariables, userId);
        setUserFoundedByIdToMap(pageVariables, userFoundedById);
        setErrorMessageToMap(pageVariables, errorMessage);
        return pageVariables;
    }

    public static long getUserIdFromMap(Map<String, Object> parameterMap) {
        if (parameterMap.get("userid") != null) {
            return (long) parameterMap.get("userid");
        } else {
            return 0;
        }
    }

    public static void setUserListToMap(Map<String, Object> pageVariables, List<UserDataSetDto> userList) {
        pageVariables.put("users", userList);
        pageVariables.put("useramount", userList.size());
    }

    public static void setErrorMessageToMap(Map<String, Object> pageVariables, String errorMessage) {
        pageVariables.put("errormessage", errorMessage);
    }

    public static void setUserIdToMap(Map<String, Object> pageVariables, long userId) {
        pageVariables.put("userid", userId);
    }

    public static void setUserFoundedByIdToMap(Map<String, Object> pageVariables, String userFoundedById) {
        pageVariables.put("userbyid", userFoundedById);
    }

    private static String getParameterFromMap(Map<String, String[]> parameterMap, String key) {
        if (parameterMap.get(key) != null) {
            return parameterMap.get(key)[0] != null ? parameterMap.get(key)[0] : "";
        } else {
            return "";
        }
    }

}
