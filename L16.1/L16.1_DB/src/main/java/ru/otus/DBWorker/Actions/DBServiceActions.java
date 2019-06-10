package ru.otus.DBWorker.Actions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static ru.otus.messages.Message.*;

public class DBServiceActions {

    public static final Map<String, Consumer> actionMap = new HashMap<>();

    static {
        actionMap.put(MESSAGE_ID_USER_LIST, new ActionGetUserList());
        actionMap.put(MESSAGE_ID_CREATE_NEW_USER, new ActionCreateNewUser());
        actionMap.put(MESSAGE_ID_FIND_USER, new ActionFindUser());
        actionMap.put(MESSAGE_ID_DELETE_USER, new ActionDeleteUser());
    }

}
