package ru.otus.dbworker.actions;

import ru.otus.dbservice.DBService;
import ru.otus.dataset.dto.UserDataSetDto;
import ru.otus.dataset.UserDataSet;
import ru.otus.exception.MyOrmException;
import ru.otus.MessageDto;
import ru.otus.messages.FEMessage;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.messages.Message.MESSAGE_ID_USER_LIST;

public class ActionFindUser implements Consumer<DBServiceActionsParameters> {

    private final static Logger logger = Logger.getLogger(ActionFindUser.class.getName());

    @Override
    public void accept(DBServiceActionsParameters dbServiceActionsParameters) {
        DBService dbService = dbServiceActionsParameters.getDbService();
        Message msg = dbServiceActionsParameters.getMsg();
        SocketMessageWorker client = dbServiceActionsParameters.getClient();
        String errorMessage = "";
        String userFoundedById = "";
        long userId = -1;
        try {
            MessageDto messageDto = msg.getMessageDto();
            UserDataSet userDataSet = dbService.getById(messageDto.getUserId(), UserDataSet.class);
            if (userDataSet != null) {
                userFoundedById = String.valueOf(new UserDataSetDto(userDataSet));
                userId = msg.getMessageDto().getUserId();
            } else {
                errorMessage = "User with id = " +  messageDto.getUserId() + " not found.";
            }
        } catch (MyOrmException e) {
            errorMessage = e.getMessage();
        }
        Message message = new FEMessage(client.getAddress(), client.getAddress(), MESSAGE_ID_USER_LIST, msg.getUuid(), new MessageDto(userId, userFoundedById, errorMessage));
        client.send(message);
        logger.log(Level.INFO, "DB Message send: " + msg.toString());
    }
}
