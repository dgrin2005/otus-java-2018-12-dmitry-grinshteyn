package ru.otus.server;

import ru.otus.messages.Message;
import ru.otus.workers.MessageWorker;
import ru.otus.workers.SocketMessageWorker;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.ServerMain.PORT_MS;

public class SocketMessageServer implements SocketMessageServerMBean {
    private final static Logger logger = Logger.getLogger(SocketMessageServer.class.getName());
    private final static int THREADS_COUNT = 1;
    private final static int MIRROR_DELAY_MS = 100;

    private final ExecutorService excecutorService;
    private final List<MessageWorker> workers;

    public SocketMessageServer(){
        logger.log(Level.INFO, "Start server");
        excecutorService = Executors.newFixedThreadPool(THREADS_COUNT);
        workers = new CopyOnWriteArrayList<>();
    }

    public void start() throws Exception{
        excecutorService.submit(this::post);
        try (ServerSocket serverSocket = new ServerSocket(PORT_MS)){
            while(!excecutorService.isShutdown()){
                Socket socket = serverSocket.accept();
                SocketMessageWorker worker = new SocketMessageWorker(socket);
                worker.init();
                workers.add(worker);
            }
        }
    }

    private void post(){
        while (true){
            for (MessageWorker worker : workers){
                Message message = worker.pool();
                if (message != null){
                    logger.log(Level.INFO, "Posting the message: " + message.toString());
                    for (MessageWorker addresse : workers) {
                        addresse.send(message);
                    }
                    message = worker.pool();
                }
            }
            try {
                Thread.sleep(MIRROR_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean getRunning(){
        return true;
    }

    @Override
    public void setRunning(boolean running){
        if (!running){
            excecutorService.shutdown();
        }
    }
}
