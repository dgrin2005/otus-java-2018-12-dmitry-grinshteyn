package ru.otus.DBWorker.Actions;

import ru.otus.DBService.DBService;
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

public class ActionDeleteUser implements Consumer<DBServiceActionsParameters> {

    private final static Logger logger = Logger.getLogger(ActionDeleteUser.class.getName());

    @Override
    public void accept(DBServiceActionsParameters dbServiceActionsParameters) {
        DBService dbService = dbServiceActionsParameters.getDbService();
        Message msg = dbServiceActionsParameters.getMsg();
        SocketMessageWorker client = dbServiceActionsParameters.getClient();
        String errorMessage = "";
        try {
            dbService.deleteById(msg.getMessageDto().getUserId(), UserDataSet.class);
        } catch (MyOrmException e) {
            errorMessage = e.getMessage();
        }
        Message message = new FEMessage(dbAddress, dbAddress, MESSAGE_ID_USER_LIST, msg.getUuid(), new MessageDto(errorMessage));
        client.send(message);
        logger.log(Level.INFO, "DB Message send: " + msg.toString());
    }
}
