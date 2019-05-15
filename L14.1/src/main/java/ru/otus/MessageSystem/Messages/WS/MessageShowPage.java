package ru.otus.MessageSystem.Messages.WS;

import ru.otus.FrontEndService.FrontEndService;
import ru.otus.MessageSystem.Address;
import ru.otus.WebServer.Dto.UserDataSetDto;

import java.util.List;
import java.util.UUID;

import static ru.otus.FrontEndService.FrontEndService.MESSAGE_ID_SHOW_PAGE;

public class MessageShowPage extends MessageToFrontEnd {

    private final List<UserDataSetDto> userListDto;
    private final String errorMessage;
    private final String userFoundedById;
    private final long userId;
    private final UUID uuid;

    public MessageShowPage(Address from, Address to,
                           List<UserDataSetDto> userListDto, String errorMessage, String userFoundedById, long userId,
                           UUID uuid) {
        super(from, to, "");
        this.userListDto = userListDto;
        this.errorMessage = errorMessage;
        this.userFoundedById = userFoundedById;
        this.userId = userId;
        this.uuid = uuid;
    }

    @Override
    public void exec(FrontEndService frontEndService) {
        frontEndService.queryPut(uuid);
        frontEndService.handleMessage(MESSAGE_ID_SHOW_PAGE, userListDto, errorMessage, userFoundedById, userId);
    }
}
