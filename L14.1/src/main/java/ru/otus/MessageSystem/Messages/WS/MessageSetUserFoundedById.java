package ru.otus.MessageSystem.Messages.WS;

import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Address;

import static ru.otus.FrontEndService.FrontEndService.MESSAGE_ID_SET_USER_FOUNDED_BY_ID;

public class MessageSetUserFoundedById extends MessageToFrontEnd {

    private final String userFoundedById;

    public MessageSetUserFoundedById(Address from, Address to, String userFoundedById) {
        super(from, to, "");
        this.userFoundedById = userFoundedById;
    }

    @Override
    public void exec(FrontEndService frontEndService) {
        frontEndService.handleMessage(MESSAGE_ID_SET_USER_FOUNDED_BY_ID, userFoundedById);
    }
}
