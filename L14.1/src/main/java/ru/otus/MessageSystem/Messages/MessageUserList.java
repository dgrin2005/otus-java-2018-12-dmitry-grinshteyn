package ru.otus.MessageSystem.Messages;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;
import ru.otus.WebServer.Dto.UserDataSetDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.WebServer.WebServerUtilites.*;

public class MessageUserList extends MessageToDB {

    private final HttpServletRequest httpServletRequest;
    private String errorMessage;
    private final String userFoundedById;
    private final long userId;
    private final Address finalAddressee;

    public MessageUserList(Address from, Address to, HttpServletRequest httpServletRequest,
                           String errorMessage, String userFoundedById, long userId, Address finalAddressee) {
        super(from, to);
        this.httpServletRequest = httpServletRequest;
        this.errorMessage = errorMessage;
        this.userFoundedById = userFoundedById;
        this.userId = userId;
        this.finalAddressee = finalAddressee;
    }

    @Override
    public void exec(DBService dbService) {
        List<UserDataSetDto> userListDto = new ArrayList<>();
        try {
            userListDto = userDataSetListToDtoList(dbService.getAll(UserDataSet.class));
        } catch (MyOrmException e) {
            errorMessage = errorMessage + "\n" +e.getMessage();
        }
        Message message = new MessageShowPage(getTo(), finalAddressee, httpServletRequest,
                userListDto, errorMessage, userFoundedById, userId);
        dbService.getMessageSystem().sendMessage(message);
    }

}
