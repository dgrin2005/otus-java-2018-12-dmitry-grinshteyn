package ru.otus.FEWorker;

import ru.otus.messages.Address;
import ru.otus.messages.FEMessage;
import ru.otus.workers.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketMessageWorker extends SocketMessageWorker {

    private final Socket socket;

    public ClientSocketMessageWorker(String host, int port, int index) throws IOException {
        this(new Socket(host, port), index);
    }

    public ClientSocketMessageWorker(Socket socket, int index) {
        super(socket, new Address(FEMessage.class.getName(), index));
        this.socket = socket;
    }

    @Override
    public void close() throws IOException {
        super.close();
        socket.close();
    }
}
