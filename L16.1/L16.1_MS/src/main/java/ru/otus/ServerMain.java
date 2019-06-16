package ru.otus;

import ru.otus.exception.MyMSException;
import ru.otus.runner.ProcessRunnerImpl;
import ru.otus.server.SocketMessageServer;

import javax.management.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ServerMain {

    private final static Logger logger = Logger.getLogger(ServerMain.class.getName());

    public static final String HOST = "localhost";
    public static final int PORT_MS = 5050;

    private static final String FESERVICE_WAR_PATH = "l16.1_fe/target/otus_hw_16_fe.war";
    private static final String FESERVICE_WEBAPPS_WAR1 = "webapps/otus_hw_16_fe_1.war";
    private static final String FESERVICE_WEBAPPS_WAR2 = "webapps/otus_hw_16_fe_2.war";
    private static final String DBSERVICE_START_COMMAND = "java -jar l16.1_db/target/otus_hw_16_db.jar ";
    private static final String FESERVICE_START_COMMAND = "java -jar ";
    private static String DBSERVICE_START_COMMAND1;
    private static String DBSERVICE_START_COMMAND2;
    private static String FESERVICE_START_COMMAND1;

    private static final int START_DELAY_SEC = 5;


    public static void main( String[] args ) throws MyMSException {
        try {
            Properties properties = new Properties();
            File file = new File("properties.properties");
            properties.load(new FileInputStream(file));
            String portDb1 = properties.getProperty("port_db1");
            String portDb2 = properties.getProperty("port_db2");
            String pathJetty = properties.getProperty("path_jetty");
            String portJetty = properties.getProperty("port_jetty");
            String hibernate1CfgXml = properties.getProperty("hibernate1_cfg_xml");
            String hibernate2CfgXml = properties.getProperty("hibernate2_cfg_xml");
            DBSERVICE_START_COMMAND1 = DBSERVICE_START_COMMAND + hibernate1CfgXml + " -port " + portDb1;
            DBSERVICE_START_COMMAND2 = DBSERVICE_START_COMMAND + hibernate2CfgXml + " -port " + portDb2;
            FESERVICE_START_COMMAND1 = FESERVICE_START_COMMAND + pathJetty + " jetty.port=" + portJetty;
            new ServerMain().start();
        } catch (Exception e) {
            throw new MyMSException(e.getMessage(), e);
        }
    }

    private void start() throws MyMSException {
        try {
            Path source = Paths.get(FESERVICE_WAR_PATH);
            Path dest1 = Paths.get(FESERVICE_WEBAPPS_WAR1);
            Path dest2 = Paths.get(FESERVICE_WEBAPPS_WAR2);
            Files.copy(source, dest1, REPLACE_EXISTING);
            Files.copy(source, dest2, REPLACE_EXISTING);

            ScheduledExecutorService executorServiceDB1 = Executors.newSingleThreadScheduledExecutor();
            startClient(executorServiceDB1, DBSERVICE_START_COMMAND1);
            ScheduledExecutorService executorServiceDB2 = Executors.newSingleThreadScheduledExecutor();
            startClient(executorServiceDB2, DBSERVICE_START_COMMAND2);
            ScheduledExecutorService executorServiceFE1 = Executors.newSingleThreadScheduledExecutor();
            startClient(executorServiceFE1, FESERVICE_START_COMMAND1);
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("ru.otus:type=Server");
            SocketMessageServer server = new SocketMessageServer();
            mBeanServer.registerMBean(server, objectName);
            server.start();
            executorServiceDB1.shutdown();
            executorServiceDB2.shutdown();
            executorServiceFE1.shutdown();
        } catch (Exception e) {
            throw new MyMSException(e.getMessage(), e);
        }
    }

    private void startClient(ScheduledExecutorService executorService, String clientStartCommand) {
        logger.log(Level.INFO, "Starting " + clientStartCommand);
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(clientStartCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, START_DELAY_SEC, TimeUnit.SECONDS);
    }
}
