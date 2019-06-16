package ru.otus.server;

import ru.otus.exception.MyMSException;
import ru.otus.messages.DBMessage;
import ru.otus.messages.FEMessage;
import ru.otus.messages.Message;
import ru.otus.workers.MessageWorker;
import ru.otus.workers.SocketMessageWorker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.ServerMain.PORT_MS;

public class SocketMessageServer implements SocketMessageServerMBean {
    private final static Logger logger = Logger.getLogger(SocketMessageServer.class.getName());
    private final static int THREADS_COUNT = 1;
    private final static int DELAY_MS = 100;

    private final ExecutorService excecutorService;
    private final List<MessageWorker> workers;

    private Map<MessageWorker, MessageWorker> addressMap = new HashMap<>();

    public SocketMessageServer() {
        logger.log(Level.INFO, "Start server");
        excecutorService = Executors.newFixedThreadPool(THREADS_COUNT);
        workers = new CopyOnWriteArrayList<>();
    }

    public void start() throws MyMSException{
        excecutorService.submit(() -> {
            try {
                post();
            } catch (MyMSException e) {
                e.printStackTrace();
            }
        });
        try (ServerSocket serverSocket = new ServerSocket(PORT_MS)){
            while(!excecutorService.isShutdown()){
                Socket socket = serverSocket.accept();
                Reader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                SocketMessageWorker worker = new SocketMessageWorker(socket);
                worker.init();
                workers.add(worker);
                worker.setAddress(((BufferedReader) reader).readLine());
                logger.log(Level.INFO, "Added worker: " + worker);
            }
        } catch (IOException e) {
            throw new MyMSException(e.getMessage(), e);
        }
    }

    private void post() throws MyMSException {
        while (true){
            for (MessageWorker worker : workers){
                Message message = worker.pool();
                if (message != null){
                    logger.log(Level.INFO, "Posting the message: " + message.toString());
                    if (message.getFrom().equals(message.getTo())) {
                        worker.send(message);
                    } else {
                        if (addressMap.containsKey(worker)) {
                            addressMap.get(worker).send(message);
                        } else {
                            MessageWorker correspondentWorker = findCorrespondentWorker((SocketMessageWorker) worker);
                            if (correspondentWorker != null) {
                                addressMap.put(worker, correspondentWorker);
                                addressMap.put(correspondentWorker, worker);
                                correspondentWorker.send(message);
                            } else {
                                throw new MyMSException("Correspondent address not found for worker " + worker);
                            }
                        }
                    }
                    message = worker.pool();
                }
            }
            try {
                Thread.sleep(DELAY_MS);
            } catch (Exception e) {
                throw new MyMSException(e.getMessage(), e);
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

    private MessageWorker findCorrespondentWorker(SocketMessageWorker worker) {
        SocketMessageWorker correspondentWorker = null;
        String correspondentClassName = "";
        if (getWorkerClassName(worker).equals(FEMessage.class.getName())) {
            correspondentClassName = DBMessage.class.getName();
        } else {
            if (getWorkerClassName(worker).equals(DBMessage.class.getName())) {
                correspondentClassName = FEMessage.class.getName();
            }
        }
        if (!correspondentClassName.isEmpty()) {
            int i = 0;
            while (correspondentWorker == null || i < workers.size()) {
                SocketMessageWorker currentWorker = (SocketMessageWorker) workers.get(i);
                if (getWorkerClassName(currentWorker).equals(correspondentClassName) &&
                        !addressMap.containsKey(currentWorker)) {
                    correspondentWorker = currentWorker;
                }
                i++;
            }
        }
        return correspondentWorker;
    }

    private String getWorkerClassName(SocketMessageWorker worker) {
        return worker.getAddress().getClassName();
    }

}
