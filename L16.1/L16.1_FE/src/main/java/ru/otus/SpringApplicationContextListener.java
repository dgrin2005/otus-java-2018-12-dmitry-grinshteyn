package ru.otus;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringApplicationContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("SpringBeans.xml");
        sce.getServletContext().setAttribute("applicationContext", ac);
    }
}
