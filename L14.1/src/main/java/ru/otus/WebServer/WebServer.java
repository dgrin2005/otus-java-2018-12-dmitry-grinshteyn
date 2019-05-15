package ru.otus.WebServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.FrontEndService.FrontEndService;

import javax.servlet.http.HttpServlet;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.WebServer.WebServerUtilites.*;

public class WebServer {

    private final static Logger logger = Logger.getLogger(WebServer.class.getName());
    private Server server;
    private HttpServlet[] httpServlets;

    public WebServer(FrontEndService frontEndService) {
        HttpServlet httpServlet = new UserDataSetServlet(frontEndService);

        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource(STATIC_PATH_RESOURCE);
        resourceHandler.setBaseResource(resource);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(httpServlet), "/userdataset");

        server = new Server(new InetSocketAddress(HOSTNAME, PORT));
        server.setHandler(new HandlerList(resourceHandler, servletContextHandler));

        httpServlets = new HttpServlet[]{httpServlet};
    }

    public HttpServlet[] getHttpServlets() {
        return httpServlets;
    }

    public void start() {
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
