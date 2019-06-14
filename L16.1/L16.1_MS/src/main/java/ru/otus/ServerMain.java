package ru.otus;

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

    public static final String HOST = "localhost";
    public static final int PORT_MS = 5050;

    private static String DBSERVICE_START_COMMAND = "java -jar L16.1_DB/target/otus_hw_16_db.jar ";
    private static String FESERVICE_START_COMMAND = "java -jar ";
    private static String DBSERVICE_START_COMMAND1;
    private static String DBSERVICE_START_COMMAND2;
    private static String FESERVICE_START_COMMAND1;
    private static String FESERVICE_START_COMMAND2;

    private static final int START_DELAY_SEC = 5;


    public static void main( String[] args ) throws Exception {
        Properties properties = new Properties();
        File file = new File("properties.properties");
        properties.load(new FileInputStream(file));
        String portDb1 = properties.getProperty("port_db1");
        String portDb2 = properties.getProperty("port_db2");
        String pathJetty = properties.getProperty("path_jetty");
        String portJetty1 = properties.getProperty("port_jetty1");
        String portJetty2 = properties.getProperty("port_jetty2");
        String hibernate1CfgXml = properties.getProperty("hibernate1_cfg_xml");
        String hibernate2CfgXml = properties.getProperty("hibernate2_cfg_xml");
        DBSERVICE_START_COMMAND1 = DBSERVICE_START_COMMAND + hibernate1CfgXml + " -port " + portDb1;
        DBSERVICE_START_COMMAND2 = DBSERVICE_START_COMMAND + hibernate2CfgXml + " -port " + portDb2;
        FESERVICE_START_COMMAND1 = FESERVICE_START_COMMAND + pathJetty + " jetty.port=" + portJetty1;
        FESERVICE_START_COMMAND2 = FESERVICE_START_COMMAND + pathJetty + " jetty.port=" + portJetty2;
        new ServerMain().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorServiceDB1 = Executors.newSingleThreadScheduledExecutor();
        startClient(executorServiceDB1, DBSERVICE_START_COMMAND1, START_DELAY_SEC);
        ScheduledExecutorService executorServiceDB2 = Executors.newSingleThreadScheduledExecutor();
        startClient(executorServiceDB2, DBSERVICE_START_COMMAND2,START_DELAY_SEC);
        ScheduledExecutorService executorServiceFE1 = Executors.newSingleThreadScheduledExecutor();
        startClient(executorServiceFE1, FESERVICE_START_COMMAND1, START_DELAY_SEC);
        ScheduledExecutorService executorServiceFE2 = Executors.newSingleThreadScheduledExecutor();
        startClient(executorServiceFE2, FESERVICE_START_COMMAND2, START_DELAY_SEC);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("ru.otus:type=Server");
        SocketMessageServer server = new SocketMessageServer();
        mBeanServer.registerMBean(server, objectName);
        server.start();
        executorServiceDB1.shutdown();
        executorServiceDB2.shutdown();
        executorServiceFE1.shutdown();
        executorServiceFE2.shutdown();
    }

    private void startClient(ScheduledExecutorService executorService, String clientStartCommand,
                             int clientStartDelaySec) {
        logger.log(Level.INFO, "Starting " + clientStartCommand);
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(clientStartCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, clientStartDelaySec, TimeUnit.SECONDS);
    }
}
