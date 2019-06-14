package ru.otus.FEWorker;

import ru.otus.messages.Address;
import ru.otus.messages.FEMessage;
import ru.otus.workers.SocketMessageWorker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketMessageWorker extends SocketMessageWorker {

    private final Socket socket;

    public ClientSocketMessageWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public ClientSocketMessageWorker(Socket socket) throws IOException {
        super(socket, new Address(FEMessage.class.getName()));
        this.socket = socket;
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(FEMessage.class.getName());
    }

    @Override
    public void close() throws IOException {
        super.close();
        socket.close();
    }
}
