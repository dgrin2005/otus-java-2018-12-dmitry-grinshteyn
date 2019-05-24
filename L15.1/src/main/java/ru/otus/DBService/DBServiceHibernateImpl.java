package ru.otus.DBService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.DAO.DataSetDAO;
import ru.otus.DAO.DataSetDAOHibernateImpl;
import ru.otus.DataSet.DataSet;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.MessageSystem;
import ru.otus.MessageSystem.MessageSystemContext;

import java.util.List;

public class DBServiceHibernateImpl implements DBService, AutoCloseable {

    private final SessionFactory sessionFactory;
    private final MessageSystemContext messageSystemContext;
    private final Address address;

    public DBServiceHibernateImpl(String configFile,
                                  MessageSystemContext messageSystemContext,
                                  Address address,
                                  Class<? extends DataSet>... classes) {
        Configuration configuration = ConfigurationBuilder.builder()
                .configFile(configFile).annotatedClasses(classes).build();
        sessionFactory = createSessionFactory(configuration);
        this.messageSystemContext = messageSystemContext;
        this.address = address;
        this.messageSystemContext.getMessageSystem().addAddressee(this);
        this.messageSystemContext.getMessageSystem().startThread(this);
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
            DataSetDAO dao = new DataSetDAOHibernateImpl(session);
            T result = dao.create(t);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOHibernateImpl(session);
            T result = dao.getById(id, t);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> List<T> getAll(Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOHibernateImpl(session);
            List<T> result = dao.getAll(t);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> void deleteById(long id, Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOHibernateImpl(session);
            dao.deleteById(id, t);
            transaction.commit();
        }
    }

    @Override
    public <T extends DataSet> void deleteAll(Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOHibernateImpl(session);
            dao.deleteAll(t);
            transaction.commit();
        }
    }

    @Override
    public <T extends DataSet> T update(T t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOHibernateImpl(session);
            T result = dao.update(t);
            transaction.commit();
            return result;
        }
    }

    @Override
    public <T extends DataSet> long count(Class<T> t) throws MyOrmException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            DataSetDAO dao = new DataSetDAOHibernateImpl(session);
            List<T> result = dao.getAll(t);
            transaction.commit();
            return result.size();
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
}
