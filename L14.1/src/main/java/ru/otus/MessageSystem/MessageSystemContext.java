package ru.otus.MessageSystem;

public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Address webServerAddress;
    private Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getWebServerAddress() {
        return webServerAddress;
    }

    public void setWebServerAddress(Address webServerAddress) {
        this.webServerAddress = webServerAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(Address dbAddress) {
        this.dbAddress = dbAddress;
    }
}
