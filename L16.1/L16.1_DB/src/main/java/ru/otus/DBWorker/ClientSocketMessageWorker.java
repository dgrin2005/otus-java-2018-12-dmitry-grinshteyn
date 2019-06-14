package ru.otus.DBWorker;

import ru.otus.messages.Address;
import ru.otus.messages.DBMessage;
import ru.otus.workers.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketMessageWorker extends SocketMessageWorker {

    private final Socket socket;

    public ClientSocketMessageWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    private ClientSocketMessageWorker(Socket socket) {
        super(socket, new Address(DBMessage.class.getName()));
        this.socket = socket;
    }

    @Override
    public void close() throws IOException {
        super.close();
        socket.close();
    }
}
