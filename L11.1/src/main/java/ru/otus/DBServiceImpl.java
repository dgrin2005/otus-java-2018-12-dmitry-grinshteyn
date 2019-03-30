package ru.otus;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.DAO.DataSetDAO;
import ru.otus.DAO.DataSetDAOImpl;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;

import java.util.List;

public class DBServiceImpl implements DBService, AutoCloseable {

    private final SessionFactory sessionFactory;

    public <T extends DataSet> DBServiceImpl(Class<T> t) {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(t);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/otus");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "123A321");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.connection.allowPublicKeyRetrieval", "true");
        configuration.setProperty("hibernate.connection.useLegacyDatetimeCode", "false");
        configuration.setProperty("hibernate.connection.serverTimezone", "UTC");

        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public void close() {
        sessionFactory.close();
    }

    @Override
    public <T extends DataSet> T create(T t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOImpl(session);
            T result = dao.create(t);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOImpl(session);
            T result = dao.getById(id, t);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOImpl(session);
            List<T> result = dao.getAll(t);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOImpl(session);
            dao.deleteById(id, t);
            transaction.commit();
        }
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOImpl(session);
            dao.deleteAll(t);
            transaction.commit();
        }
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOImpl(session);
            T result = dao.update(t);
            transaction.commit();
            return result;
        }
    }

}
