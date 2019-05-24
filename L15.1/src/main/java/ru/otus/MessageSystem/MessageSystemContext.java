package ru.otus.MessageSystem;

public class MessageSystemContext {
    private final MessageSystem messageSystem;
    private final Address webServerAddress;
    private final Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem, Address webServerAddress, Address dbAddress) {
        this.messageSystem = messageSystem;
        this.webServerAddress = webServerAddress;
        this.dbAddress = dbAddress;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getWebServerAddress() {
        return webServerAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }
}
