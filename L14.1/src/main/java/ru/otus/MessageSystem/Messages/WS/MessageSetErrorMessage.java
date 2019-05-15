package ru.otus.MessageSystem.Messages.WS;

import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Address;

import static ru.otus.FrontEndService.FrontEndService.MESSAGE_ID_SET_ERROR_MESSAGE;

public class MessageSetErrorMessage extends MessageToFrontEnd {

    private final String errorMessage;

    public MessageSetErrorMessage(Address from, Address to, String errorMessage) {
        super(from, to, "");
        this.errorMessage = errorMessage;
    }

    @Override
    public void exec(FrontEndService frontEndService) {
        frontEndService.handleMessage(MESSAGE_ID_SET_ERROR_MESSAGE, errorMessage);
    }
}
