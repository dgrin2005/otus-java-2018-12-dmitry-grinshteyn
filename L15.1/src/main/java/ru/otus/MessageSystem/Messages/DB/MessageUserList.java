package ru.otus.MessageSystem.Messages.DB;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Message;
import ru.otus.MessageSystem.Messages.FE.MessageShowPage;
import ru.otus.FrontEnd.Dto.UserDataSetDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.otus.FrontEnd.FrontEndUtilites.*;

public class MessageUserList extends MessageToDB {

    private String errorMessage;
    private final String userFoundedById;
    private final long userId;
    private final Address finalAddressee;
    private final UUID uuid;

    public MessageUserList(Address from, Address to,
                           Address finalAddressee, UUID uuid) {
        super(from, to);
        this.errorMessage = "";
        this.userFoundedById = "";
        this.userId = -1;
        this.finalAddressee = finalAddressee;
        this.uuid = uuid;
    }

    MessageUserList(Address from, Address to,
                    Address finalAddressee, String errorMessage, UUID uuid) {
        super(from, to);
        this.errorMessage = errorMessage;
        this.userFoundedById = "";
        this.userId = -1;
        this.finalAddressee = finalAddressee;
        this.uuid = uuid;
    }

    MessageUserList(Address from, Address to,
                    Address finalAddressee, String errorMessage, String userFoundedById, long userId, UUID uuid) {
        super(from, to);
        this.errorMessage = errorMessage;
        this.userFoundedById = userFoundedById;
        this.userId = userId;
        this.finalAddressee = finalAddressee;
        this.uuid = uuid;
    }

    @Override
    public void exec(DBService dbService) {
        List<UserDataSetDto> userListDto = new ArrayList<>();
        try {
            userListDto = userDataSetListToDtoList(dbService.getAll(UserDataSet.class));
        } catch (MyOrmException e) {
            errorMessage = errorMessage + "\n" +e.getMessage();
        }
        Message message = new MessageShowPage(getTo(), finalAddressee,
                userListDto, errorMessage, userFoundedById, userId, uuid);
        dbService.getMessageSystem().sendMessage(message);
    }

}
