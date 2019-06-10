package ru.otus.DBWorker.Actions;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.Dto.UserDataSetDto;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageDto;
import ru.otus.messages.FEMessage;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.ServerMain.dbAddress;
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
        Message message = new FEMessage(dbAddress, dbAddress, MESSAGE_ID_USER_LIST, msg.getUuid(), new MessageDto(userId, userFoundedById, errorMessage));
        client.send(message);
        logger.log(Level.INFO, "DB Message send: " + msg.toString());
    }
}
