package ru.otus.WebServer.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

public interface UserDataSetService {

    void doAction(String action, UUID uuid, Map<String, Object> pageVariables);
    void actionCreateNewUser(HttpServletRequest req, UUID uuid, Map<String, Object> pageVariables);
    void actionDeleteUser(UUID uuid, Map<String, Object> pageVariables);
    void actionFindUser(UUID uuid, Map<String, Object> pageVariables);
    void actionUserList(UUID uuid, Map<String, Object> pageVariables);

}
