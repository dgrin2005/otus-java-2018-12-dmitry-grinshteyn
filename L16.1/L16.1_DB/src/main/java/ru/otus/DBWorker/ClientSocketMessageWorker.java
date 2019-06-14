package ru.otus.DBWorker;

import ru.otus.exception.MyMSException;
import ru.otus.messages.Address;
import ru.otus.messages.DBMessage;
import ru.otus.workers.SocketMessageWorker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketMessageWorker extends SocketMessageWorker {

    private final Socket socket;

    public ClientSocketMessageWorker(String host, int port) throws IOException, MyMSException {
        this(new Socket(host, port));
    }

    private ClientSocketMessageWorker(Socket socket) throws MyMSException {
        super(socket, new Address(DBMessage.class.getName()));
        try {
            this.socket = socket;
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(DBMessage.class.getName());
        } catch (IOException e) {
            throw new MyMSException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws MyMSException {
        super.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new MyMSException(e.getMessage(), e);
        }
    }
}
