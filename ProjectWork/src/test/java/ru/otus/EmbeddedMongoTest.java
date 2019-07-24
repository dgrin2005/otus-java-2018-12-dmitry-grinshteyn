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
import ru.otus.exception.MongoODMException;
import ru.otus.executor.Executor;
import ru.otus.executor.MongoExecutor;

public class EmbeddedMongoTest
{
    private static final String DATABASE_NAME = "embedded";

    private MongodExecutable mongodExecutable;
    private MongodProcess mongodProcess;
    private MongoDatabase mongoDatabase;
    private Executor mongoExecutor;

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
        this.mongodExecutable = starter.prepare(mongodConfig);
        this.mongodProcess = mongodExecutable.start();
        MongoClient mongoClient = MongoClients.create("mongodb://" + bindIp + ":" + port);
        this.mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);
        this.mongoExecutor = new MongoExecutor(mongoDatabase);
        creatingTestingData(mongoDatabase);
    }

    @After
    public void afterEach() throws Exception {
        if (this.mongodProcess != null) {
            this.mongodProcess.stop();
            this.mongodExecutable.stop();
        }
    }

    @Test
    public void testCreateUsers() throws MongoODMException {
        MongoCollection<Document> ads = mongoDatabase.getCollection("uds", Document.class);
        assertEquals(3L, ads.countDocuments());
    }

    @Test
    public void testLoadById() throws MongoODMException {
        assertEquals(testUser1, mongoExecutor.load(testUser1.getId(), UserDataSet.class));
    }

    @Test
    public void testLoadByName() throws MongoODMException {
        assertEquals(testUser1, mongoExecutor.loadByName(testUser1.getName(), UserDataSet.class));
    }

    @Test
    public void testUpdate() throws MongoODMException {
        testUser1.setName("Maria");
        testUser1.setAddress(new AddressDataSet("Kirova"));
        testUser1.setPhones(Arrays.asList(new PhoneDataSet("12345"), new PhoneDataSet("54321")));
        mongoExecutor.update(testUser1);
        assertEquals(testUser1, mongoExecutor.loadByName(testUser1.getName(), UserDataSet.class));
    }

    @Test
    public void testDeleteById() throws MongoODMException {
        mongoExecutor.deleteById(UserDataSet.class, testUser1.getId());
        assertNull(mongoExecutor.load(testUser1.getId(), UserDataSet.class));
    }

    @Test
    public void testDeleteByName() throws MongoODMException {
        mongoExecutor.deleteByName(UserDataSet.class, testUser1.getName());
        assertNull(mongoExecutor.loadByName(testUser1.getName(), UserDataSet.class));
    }

    @Test
    public void testDeleteList() throws MongoODMException {
        mongoExecutor.deleteList(Arrays.asList(testUser1, testUser3));
        assertNull(mongoExecutor.load(testUser1.getId(), UserDataSet.class));
        assertNull(mongoExecutor.load(testUser3.getId(), UserDataSet.class));
    }

    @Test
    public void testDeleteAll() throws MongoODMException {
        MongoCollection<Document> uds = mongoDatabase.getCollection("uds", Document.class);
        mongoExecutor.deleteAll(UserDataSet.class);
        assertEquals(0L, uds.countDocuments());
    }

    @Test
    public void testEqual() throws MongoODMException {
        assertEquals(2, mongoExecutor.loadWhenEqual(UserDataSet.class, "address", testAddress).size());
        assertEquals(1, mongoExecutor.loadWhenEqual(UserDataSet.class, "name", "Ivan").size());
        assertEquals(testUser1, mongoExecutor.loadWhenEqual(UserDataSet.class, "name", "Ivan").get(0));
    }

    @Test
    public void testNotEqual() throws MongoODMException {
        assertEquals(1, mongoExecutor.loadWhenNotEqual(UserDataSet.class, "age", 23).size());
    }

    @Test
    public void testGreaterThan() throws MongoODMException {
        assertEquals(1, mongoExecutor.loadWhenGreaterThan(UserDataSet.class, "age", 23).size());
    }

    @Test
    public void testLessThan() throws MongoODMException {
        assertEquals(2, mongoExecutor.loadWhenLessThan(UserDataSet.class, "age", 30).size());
    }

    private void creatingTestingData(MongoDatabase db) throws MongoODMException {
        db.createCollection("uds");
        db.createCollection("pds");
        db.createCollection("ads");
        mongoExecutor.save(testAddress);
        mongoExecutor.save(testPhone);
        mongoExecutor.save(testUser1);
        mongoExecutor.save(testUser2);
        mongoExecutor.save(testUser3);
    }

}