package ru.otus.FEWorker.Actions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static ru.otus.messages.Message.MESSAGE_ID_SHOW_PAGE;

public class FEServiceActions {

    public static final Map <String, Consumer> actionMap = new HashMap<>();

    static {
        actionMap.put(MESSAGE_ID_SHOW_PAGE, new ActionShowPage());
    }

}
