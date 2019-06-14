package ru.otus.workers;

import ru.otus.exception.MyMSException;
import ru.otus.messages.Message;

import java.io.IOException;

public interface MessageWorker {

    Message pool();

    void send(Message message);

    Message take() throws MyMSException;

    void close() throws MyMSException;
}
