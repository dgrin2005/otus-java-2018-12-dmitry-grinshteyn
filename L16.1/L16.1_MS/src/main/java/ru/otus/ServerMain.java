package ru.otus;

import ru.otus.messages.Address;
import ru.otus.messages.DBMessage;
import ru.otus.messages.FEMessage;
import ru.otus.runner.ProcessRunnerImpl;
import ru.otus.server.SocketMessageServer;

import javax.management.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

    private final static Logger logger = Logger.getLogger(ServerMain.class.getName());

    public static String HOST;
    public static int PORT_MS = 5050;

    private static String DBSERVICE_START_COMMAND = "java -jar L16.1_DB/target/OTUS_HW_16_DB.jar -port ";
    private static String FESERVICE_START_COMMAND = "java -jar ";
    private static final int CLIENT_START_DELAY_SEC = 5;

    public static final Address feAddress = new Address(FEMessage.class.getName());
    public static final Address dbAddress = new Address(DBMessage.class.getName());

    public static void main( String[] args ) throws Exception {
        Properties properties = new Properties();
        File file = new File("properties.properties");
        properties.load(new FileInputStream(file));
        HOST = properties.getProperty("host");
        PORT_MS = Integer.parseInt(properties.getProperty("port_ms"));
        String portDb = properties.getProperty("port_db");
        String portJetty = properties.getProperty("port_jetty");
        String pathJetty = properties.getProperty("path_jetty");
        DBSERVICE_START_COMMAND = DBSERVICE_START_COMMAND + portDb;
        FESERVICE_START_COMMAND = FESERVICE_START_COMMAND + pathJetty + " jetty.port=" + portJetty;
        new ServerMain().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorServiceDB = Executors.newSingleThreadScheduledExecutor();
        startClient(executorServiceDB, DBSERVICE_START_COMMAND);
        ScheduledExecutorService executorServiceFE = Executors.newSingleThreadScheduledExecutor();
        startClient(executorServiceFE, FESERVICE_START_COMMAND);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("ru.otus:type=Server");
        SocketMessageServer server = new SocketMessageServer();
        mBeanServer.registerMBean(server, objectName);
        server.start();
        executorServiceDB.shutdown();
        executorServiceFE.shutdown();

    }

    private void startClient(ScheduledExecutorService executorService, String clientStartCommand) {
        logger.log(Level.INFO, "Starting " + clientStartCommand);
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(clientStartCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }
}
