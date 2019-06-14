package ru.otus.workers;

import ru.otus.messages.Message;

import java.io.IOException;

public interface MessageWorker {

    Message pool();

    void send(Message message);

    Message take() throws InterruptedException;

    void close() throws IOException;
}
