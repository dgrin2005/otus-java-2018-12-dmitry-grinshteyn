package ru.otus.DBWorker.Actions;

import ru.otus.DBService.DBService;
import ru.otus.DataSet.PhoneDataSet;
import ru.otus.DataSet.UserDataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageDto;
import ru.otus.messages.Address;
import ru.otus.messages.DBMessage;
import ru.otus.messages.FEMessage;
import ru.otus.messages.Message;
import ru.otus.workers.SocketMessageWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ru.otus.messages.Message.MESSAGE_ID_SHOW_PAGE;

public class ActionGetUserList implements Consumer<DBServiceActionsParameters> {

    private final static Logger logger = Logger.getLogger(ActionGetUserList.class.getName());

    @Override
    public void accept(DBServiceActionsParameters dbServiceActionsParameters) {
        DBService dbService = dbServiceActionsParameters.getDbService();
        Message msg = dbServiceActionsParameters.getMsg();
        SocketMessageWorker client = dbServiceActionsParameters.getClient();
        String errorMessage = msg.getMessageDto().getErrorMessage();
        String userFoundedById = msg.getMessageDto().getUserFoundedById();
        long userId = msg.getMessageDto().getUserId();
        final String finalErrorMessage = errorMessage;
        List<MessageDto> messageDtoList = new ArrayList<>();
        try {
            List<UserDataSet> userDataSetList = dbService.getAll(UserDataSet.class);
            messageDtoList = userDataSetList.stream().map(x -> new MessageDto(
                    x.getId(),
                    x.getName(),
                    x.getAge(),
                    x.getAddress().getStreet(),
                    x.getPhones().stream()
                            .map(PhoneDataSet::getNumber)
                            .collect(Collectors.joining(", ")),
                    finalErrorMessage
            )).collect(Collectors.toList());
        } catch (MyOrmException e) {
            errorMessage = errorMessage + "\n" +e.getMessage();
        }
        Message message = new DBMessage(client.getAddress(), new Address(FEMessage.class.getName()), MESSAGE_ID_SHOW_PAGE, msg.getUuid(), new MessageDto(userId, userFoundedById, errorMessage), messageDtoList);
        client.send(message);
        logger.log(Level.INFO, "DB Message send: " + msg.toString());
    }
}
