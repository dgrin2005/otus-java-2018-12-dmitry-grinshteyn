package ru.otus;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import java.util.Arrays;

import org.bson.Document;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ru.otus.dataset.AddressDataSet;
import ru.otus.dataset.PhoneDataSet;
import ru.otus.dataset.UserDataSet;
import ru.otus.exception.MyOrmException;
import ru.otus.executor.Executor;

public class EmbeddedMongoTest
{
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongo;

    private AddressDataSet testAddress = new AddressDataSet("Lenina");

    private PhoneDataSet testPhone = new PhoneDataSet("3333");

    private UserDataSet testUser1 = new UserDataSet("Ivan", 23,
            testAddress,
            Arrays.asList(new PhoneDataSet("6666", "5555"), testPhone));

    private UserDataSet testUser2 = new UserDataSet("Aleksey", 35,
            testAddress,
            Arrays.asList(new PhoneDataSet("6666", "5555"), testPhone));

    private UserDataSet testUser3 = new UserDataSet("Maria", 23,
            new AddressDataSet("Kirova"),
            Arrays.asList(new PhoneDataSet("6666", "5555"), testPhone));

    @Before
    public void beforeEach() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        String bindIp = "localhost";
        int port = 12345;
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();
        this.mongodExe = starter.prepare(mongodConfig);
        this.mongod = mongodExe.start();
        this.mongo = MongoClients.create("mongodb://" + bindIp + ":" + port);
    }

    @After
    public void afterEach() throws Exception {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExe.stop();
        }
    }

    @Test
    public void testCreateUsers() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        MongoCollection<Document> ads = db.getCollection("uds", Document.class);
        assertEquals(3L, ads.countDocuments());
    }

    @Test
    public void testLoadById() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        assertEquals(testUser1, Executor.load(db, testUser1.getId(), UserDataSet.class));
    }

    @Test
    public void testLoadByName() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        assertEquals(testUser1, Executor.loadByName(db, testUser1.getName(), UserDataSet.class));
    }

    @Test
    public void testUpdate() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        testUser1.setName("Maria");
        testUser1.setAddress(new AddressDataSet("Kirova"));
        testUser1.setPhones(Arrays.asList(new PhoneDataSet("12345"), new PhoneDataSet("54321")));
        Executor.update(db, testUser1);
        assertEquals(testUser1, Executor.loadByName(db, testUser1.getName(), UserDataSet.class));
    }

    @Test
    public void testDeleteById() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        Executor.deleteById(db, UserDataSet.class, testUser1.getId());
        assertNull(Executor.load(db, testUser1.getId(), UserDataSet.class));
    }

    @Test
    public void testDeleteByName() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        Executor.deleteByName(db, UserDataSet.class, testUser1.getName());
        assertNull(Executor.loadByName(db, testUser1.getName(), UserDataSet.class));
    }

    @Test
    public void testDeleteList() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        Executor.deleteList(db, Arrays.asList(testUser1, testUser3));
        assertNull(Executor.load(db, testUser1.getId(), UserDataSet.class));
        assertNull(Executor.load(db, testUser3.getId(), UserDataSet.class));
    }

    @Test
    public void testDeleteAll() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        MongoCollection<Document> uds = db.getCollection("uds", Document.class);
        Executor.deleteAll(db, UserDataSet.class);
        assertEquals(0L, uds.countDocuments());
    }

    @Test
    public void testEqual() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        assertEquals(2, Executor.loadWhenEqual(db, UserDataSet.class, "address", testAddress).size());
        assertEquals(1, Executor.loadWhenEqual(db, UserDataSet.class, "name", "Ivan").size());
        assertEquals(testUser1, Executor.loadWhenEqual(db, UserDataSet.class, "name", "Ivan").get(0));
    }

    @Test
    public void testNotEqual() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        assertEquals(1, Executor.loadWhenNotEqual(db, UserDataSet.class, "age", 23).size());
    }

    @Test
    public void testGreaterThan() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        assertEquals(1, Executor.loadWhenGreaterThan(db, UserDataSet.class, "age", 23).size());
    }

    @Test
    public void testLessThan() throws MyOrmException {
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        creatingTestingData(db);
        assertEquals(2, Executor.loadWhenLessThan(db, UserDataSet.class, "age", 30).size());
    }

    private void creatingTestingData(MongoDatabase db) throws MyOrmException {
        db.createCollection("uds");
        db.createCollection("pds");
        db.createCollection("ads");
        Executor.save(db, testAddress);
        Executor.save(db, testPhone);
        Executor.save(db, testUser1);
        Executor.save(db, testUser2);
        Executor.save(db, testUser3);
    }



}