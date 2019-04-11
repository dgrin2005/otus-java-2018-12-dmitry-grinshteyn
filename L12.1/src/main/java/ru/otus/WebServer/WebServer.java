package ru.otus.WebServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.DBService.DBService;

import static ru.otus.WebServer.WebServerUtilites.*;

public class WebServer {

    private DBService dbService;

    public WebServer(DBService dbService) {
        this.dbService = dbService;
    }

    public void start() throws Exception {

        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource(STATIC_PATH_RESOURCE);
        resourceHandler.setBaseResource(resource);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        servletContextHandler.addServlet(new ServletHolder(new UserDataSetServlet(dbService)), "/userdataset");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, servletContextHandler));

        server.start();
        server.join();
    }
}
