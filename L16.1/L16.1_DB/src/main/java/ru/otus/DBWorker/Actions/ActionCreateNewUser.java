package ru.otus.dbworker.actions;

import ru.otus.dbservice.DBService;
import ru.otus.dataset.AddressDataSet;
import ru.otus.dataset.PhoneDataSet;
import ru.otus.dataset.UserDataSet;
import ru.otus.exception.MyOrmException;
import ru.otus.MessageDto;
import ru.otus.messages.FEMessage;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.messages.Message.MESSAGE_ID_USER_LIST;

public class ActionCreateNewUser implements Consumer<DBServiceActionsParameters> {

    private final static Logger logger = Logger.getLogger(ActionCreateNewUser.class.getName());

    @Override
    public void accept(DBServiceActionsParameters dbServiceActionsParameters) {
        DBService dbService = dbServiceActionsParameters.getDbService();
        Message msg = dbServiceActionsParameters.getMsg();
        SocketMessageWorker client = dbServiceActionsParameters.getClient();
        String errorMessage = "";
        try {
            MessageDto messageDto = msg.getMessageDto();
            UserDataSet userDataSet = new UserDataSet(messageDto.getName(),
                    messageDto.getAge(),
                    new AddressDataSet(messageDto.getAddress()),
                    new ArrayList<PhoneDataSet>(){{
                        add(new PhoneDataSet(messageDto.getPhones()));
                    }}
            );
            dbService.create(userDataSet);
        } catch (MyOrmException e) {
            errorMessage = e.getMessage();
        }
        Message message = new FEMessage(client.getAddress(), client.getAddress(), MESSAGE_ID_USER_LIST, msg.getUuid(), new MessageDto(errorMessage));
        client.send(message);
        logger.log(Level.INFO, "DB Message send: " + msg.toString());
    }
}
