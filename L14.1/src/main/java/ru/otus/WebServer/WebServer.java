package ru.otus.WebServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Addressee;
import ru.otus.MessageSystem.MessageSystem;
import ru.otus.MessageSystem.MessageSystemContext;

import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.WebServer.WebServerUtilites.*;

public class WebServer implements Addressee {

    private final static Logger logger = Logger.getLogger(WebServer.class.getName());
    private Server server;
    private final MessageSystemContext messageSystemContext;
    private final Address address;
    private final HttpServlet httpServlet;

    public WebServer(MessageSystemContext messageSystemContext, Address address) throws IOException {
        this.messageSystemContext = messageSystemContext;
        this.address = address;
        httpServlet = new UserDataSetServlet(messageSystemContext, address);
    }

    @Override
    public void initInMessageSystem() {
        messageSystemContext.getMessageSystem().addAddressee(this);
    }

    public void start() {

        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource(STATIC_PATH_RESOURCE);
        resourceHandler.setBaseResource(resource);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        servletContextHandler.addServlet(new ServletHolder(httpServlet), "/userdataset");

        server = new Server(new InetSocketAddress(HOSTNAME, PORT));
        server.setHandler(new HandlerList(resourceHandler, servletContextHandler));

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
            logger.log(Level.INFO, "Web server failed to run.");
        } finally {
            stop();
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystemContext.getMessageSystem();
    }

    public HttpServlet getHttpServlet() {
        return httpServlet;
    }

    public void stop() {
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
            logger.log(Level.INFO, "Error stopping web server.");
        }
    }
}
