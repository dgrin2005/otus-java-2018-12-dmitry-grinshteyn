package ru.otus.FrontEnd;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.FrontEnd.Service.UserDataSetService;
import ru.otus.FrontEnd.Service.UserDataSetServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static ru.otus.FrontEnd.FrontEndUtilites.*;

public class UserDataSetServlet extends HttpServlet {

    private static final String PAGE_TEMPLATE = "userdataset.html";
    private final TemplateProcessor templateProcessor;
    private UserDataSetService userDataSetService;

    public UserDataSetServlet() {
        this.templateProcessor = new TemplateProcessor();
    }

    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");
        userDataSetService = context.getBean("userDataSetService", UserDataSetServiceImpl.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> pageVariables = pageVariablesForUsersList();
        setUserIdToMap(pageVariables, getUserIdFromRequest(req));
        userDataSetService.doAction(getAction(req), uuid, pageVariables);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> pageVariables = pageVariablesForUsersList();
        userDataSetService.actionCreateNewUser(req, uuid, pageVariables);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
