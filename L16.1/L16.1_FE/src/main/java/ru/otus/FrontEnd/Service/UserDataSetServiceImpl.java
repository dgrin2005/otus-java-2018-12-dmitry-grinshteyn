package ru.otus.FrontEnd.Service;

import ru.otus.DataSet.UserDataSet;
import ru.otus.DataSet.Dto.UserDataSetDto;
import ru.otus.FrontEnd.FrontEndService;
import ru.otus.MessageDto;
import ru.otus.messages.DBMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.otus.FrontEnd.FrontEndUtilites.*;
import static ru.otus.FrontEnd.FrontEndUtilites.setErrorMessageToMap;
import static ru.otus.FrontEnd.FrontEndUtilites.setUserListToMap;
import static ru.otus.messages.Message.*;

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
            DBMessage message = (DBMessage) frontEndService.sendMessage(MESSAGE_ID_CREATE_NEW_USER, userDataSet, uuid);
            List<MessageDto> messageDtoList = message.getMessageDtoList();
            List<UserDataSetDto> userDataSetDtoList = messageDtoList.stream().map(UserDataSetDto::fromMessageDto).collect(Collectors.toList());
            setUserListToMap(pageVariables, userDataSetDtoList);
            setErrorMessageToMap(pageVariables, message.getMessageDto().getErrorMessage());
        } else {
            setUserIdToMap(pageVariables, -1);
            actionUserList(uuid, pageVariables);
            setErrorMessageToMap(pageVariables, ERROR_FIELDS_NOT_FILLED);
        }
    }

    @Override
    public void actionDeleteUser(UUID uuid, Map<String, Object> pageVariables) {
        DBMessage message = (DBMessage) frontEndService.sendMessage(MESSAGE_ID_DELETE_USER, getUserIdFromMap(pageVariables), uuid);
        List<MessageDto> messageDtoList = message.getMessageDtoList();
        List<UserDataSetDto> userDataSetDtoList = messageDtoList.stream().map(UserDataSetDto::fromMessageDto).collect(Collectors.toList());
        setUserListToMap(pageVariables, userDataSetDtoList);
        setUserIdToMap(pageVariables, -1);
        setErrorMessageToMap(pageVariables, message.getMessageDto().getErrorMessage());
    }

    @Override
    public void actionFindUser(UUID uuid, Map<String, Object> pageVariables) {
        DBMessage message = (DBMessage) frontEndService.sendMessage(MESSAGE_ID_FIND_USER, getUserIdFromMap(pageVariables), uuid);
        List<MessageDto> messageDtoList = message.getMessageDtoList();
        List<UserDataSetDto> userDataSetDtoList = messageDtoList.stream().map(UserDataSetDto::fromMessageDto).collect(Collectors.toList());
        setUserListToMap(pageVariables, userDataSetDtoList);
        setUserIdToMap(pageVariables, message.getMessageDto().getUserId());
        setUserFoundedByIdToMap(pageVariables, message.getMessageDto().getUserFoundedById());
        setErrorMessageToMap(pageVariables, message.getMessageDto().getErrorMessage());
    }

    @Override
    public void actionUserList(UUID uuid, Map<String, Object> pageVariables) {
        DBMessage message = (DBMessage) frontEndService.sendMessage(MESSAGE_ID_USER_LIST, uuid);
        List<MessageDto> messageDtoList = message.getMessageDtoList();
        List<UserDataSetDto> userDataSetDtoList = messageDtoList.stream().map(UserDataSetDto::fromMessageDto).collect(Collectors.toList());
        setUserListToMap(pageVariables, userDataSetDtoList);
        setErrorMessageToMap(pageVariables, message.getMessageDto().getErrorMessage());
    }

}
