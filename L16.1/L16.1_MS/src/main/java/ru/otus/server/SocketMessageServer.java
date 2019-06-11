package ru.otus.server;

import ru.otus.messages.Message;
import ru.otus.workers.MessageWorker;
import ru.otus.workers.SocketMessageWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

    private List<SocketMessageWorker> workersDB;
    private List<SocketMessageWorker> workersFE;

    public SocketMessageServer() throws IOException {
        logger.log(Level.INFO, "Start server");
        excecutorService = Executors.newFixedThreadPool(THREADS_COUNT);
        workers = new CopyOnWriteArrayList<>();
        workersDB = new ArrayList<>();
        workersFE = new ArrayList<>();
    }

    public void start() throws Exception{
        excecutorService.submit(this::post);
        try (ServerSocket serverSocket = new ServerSocket(PORT_MS)){
            while(!excecutorService.isShutdown()){
                Socket socket = serverSocket.accept();
                SocketMessageWorker worker = new SocketMessageWorker(socket);
                worker.init();
                workers.add(worker);
                if (workers.size() <= 2) {
                    workersDB.add(worker);
                    logger.log(Level.INFO, "Added DBService worker № " + workers.size());
                } else {
                    workersFE.add(worker);
                    logger.log(Level.INFO, "Added FEService worker № " + (workers.size() - 2));
                }
                worker.setAddress();
            }
        }
    }

    private void post(){
        while (true){
            for (MessageWorker worker : workers){
                Message message = worker.pool();
                if (message != null){
                    logger.log(Level.INFO, "Posting the message: " + message.toString());
                    for (SocketMessageWorker addresse : workersDB) {
                        if (message.getTo().equals(addresse.getAddress())) {
                            addresse.send(message);
                        }
                    }
                    for (SocketMessageWorker addresse : workersFE) {
                        if (message.getTo().equals(addresse.getAddress())) {
                            addresse.send(message);
                        }
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
