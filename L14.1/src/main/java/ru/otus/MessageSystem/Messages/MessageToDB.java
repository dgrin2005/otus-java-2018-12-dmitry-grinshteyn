package ru.otus.MessageSystem.Messages;

import ru.otus.DBService.DBService;
import ru.otus.Exception.MyOrmException;
import ru.otus.MessageSystem.Address;
import ru.otus.MessageSystem.Addressee;
import ru.otus.MessageSystem.Message;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MessageToDB extends Message {

    private final static Logger logger = Logger.getLogger(MessageToDB.class.getName());

    MessageToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) throws MyOrmException {
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        } else {
            logger.log(Level.INFO, "Wrong addressee (" + addressee + ") in messages for DB.");
        }
    }

    public abstract void exec(DBService dbService) throws MyOrmException;
}
