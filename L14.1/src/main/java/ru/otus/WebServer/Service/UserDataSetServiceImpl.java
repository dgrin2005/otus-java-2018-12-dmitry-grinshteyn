package ru.otus.WebServer.Service;

import ru.otus.DataSet.UserDataSet;
import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Messages.WS.MessageShowPage;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

import static ru.otus.FrontEndService.FrontEndService.*;
import static ru.otus.WebServer.WebServerUtilites.*;
import static ru.otus.WebServer.WebServerUtilites.setErrorMessageToMap;
import static ru.otus.WebServer.WebServerUtilites.setUserListToMap;

public class UserDataSetServiceImpl implements UserDataSetService {

    private final FrontEndService frontEndService;

    public UserDataSetServiceImpl(FrontEndService frontEndService) {
        this.frontEndService = frontEndService;
    }

    @Override
    public void doAction(String action, UUID uuid, Map<String, Object> pageVariables) {
        setUserFoundedByIdToMap(pageVariables, "");
        if (!action.isEmpty() && getUserIdFromMap(pageVariables) > 0) {
            if (action.equals(PARAMETER_ACTION_VALUE_DELETE)) {
                actionDeleteUser(uuid, pageVariables);
            } else {
                setUserIdToMap(pageVariables, -1);
                actionUserList(uuid, pageVariables);
            }
        } else {
            if (getUserIdFromMap(pageVariables) > 0) {
                actionFindUser(uuid, pageVariables);
            } else {
                setUserIdToMap(pageVariables, -1);
                actionUserList(uuid, pageVariables);
            }
        }
    }

    @Override
    public void actionCreateNewUser(HttpServletRequest req, UUID uuid, Map<String, Object> pageVariables) {
        setUserIdToMap(pageVariables, -1);
        UserDataSet userDataSet = getUserDataSetFromRequest(req);
        if (userDataSet != null) {
            MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_CREATE_NEW_USER, userDataSet, uuid);
            setUserListToMap(pageVariables, message.getUserListDto());
            setErrorMessageToMap(pageVariables, message.getErrorMessage());
        } else {
            setUserIdToMap(pageVariables, -1);
            actionUserList(uuid, pageVariables);
            setErrorMessageToMap(pageVariables, ERROR_FIELDS_NOT_FILLED);
        }
    }

    @Override
    public void actionDeleteUser(UUID uuid, Map<String, Object> pageVariables) {
        MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_DELETE_USER, getUserIdFromMap(pageVariables), uuid);
        setUserIdToMap(pageVariables, -1);
        setUserListToMap(pageVariables, message.getUserListDto());
        setErrorMessageToMap(pageVariables, message.getErrorMessage());
    }

    @Override
    public void actionFindUser(UUID uuid, Map<String, Object> pageVariables) {
        MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_FIND_USER, getUserIdFromMap(pageVariables), uuid);
        setUserListToMap(pageVariables, message.getUserListDto());
        setUserIdToMap(pageVariables, message.getUserId());
        setUserFoundedByIdToMap(pageVariables, message.getUserFoundedById());
        setErrorMessageToMap(pageVariables, message.getErrorMessage());
    }

    @Override
    public void actionUserList(UUID uuid, Map<String, Object> pageVariables) {
        MessageShowPage message = (MessageShowPage)frontEndService.sendMessage(MESSAGE_ID_USER_LIST, uuid);
        setUserListToMap(pageVariables, message.getUserListDto());
        setErrorMessageToMap(pageVariables, message.getErrorMessage());
    }

}
