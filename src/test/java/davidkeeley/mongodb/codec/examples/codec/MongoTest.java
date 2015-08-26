package davidkeeley.mongodb.codec.examples.codec;

import static org.junit.Assert.*;
import static com.mongodb.client.model.Filters.eq;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.BsonType;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.IntegerCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;

import davidkeeley.mongodb.codec.examples.entity.Team;
import davidkeeley.mongodb.codec.examples.entity.Person;
import davidkeeley.mongodb.codec.examples.provider.TeamCodecProvider;
import davidkeeley.mongodb.codec.examples.provider.PersonCodecProvider;

public class MongoTest {

    private MongoClient mc;
    private MongoCollection<Team> collection;

    @Before
    public void setUp() {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new PersonCodecProvider(),
                        new TeamCodecProvider()), MongoClient
                        .getDefaultCodecRegistry());

        assertNotNull(codecRegistry.get(Team.class));
        assertNotNull(codecRegistry.get(Person.class));

        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();

        mc = new MongoClient("localhost:27017", options);

        collection = mc.getDatabase("test_database").getCollection(
                "test_collection", Team.class);
    }

    @After
    public void tearDown() {
        mc.close();
    }

    @Test
    public void testWithoutNulls() {
        // create test team
        ObjectId objectId = new ObjectId();
        Date dateFormed = new Date();
        Person coach = new Person("testCoach", new Integer(32));
        Person assistantCoach = new Person("testAssistant", new Integer(25));
        ArrayList<Person> players = new ArrayList<Person>();
        players.add(new Person("testPlayer1", new Integer(14)));
        players.add(new Person("testPlayer2", new Integer(15)));
        Team testTeam = new Team(objectId, "testTeamName", new Integer(2),
                new Integer(0), dateFormed, coach, assistantCoach, players);

        // insert team
        collection.insertOne(testTeam);

        // query for inserted team
        Team decodedResult = collection.find(eq(Team.ID, objectId)).first();
        assertNotNull(decodedResult);

        // check result for correctness
        assertEquals(objectId, decodedResult.getId());
        assertEquals("testTeamName", decodedResult.getTeamName());
        assertEquals(new Integer(2), decodedResult.getWins());
        assertEquals(new Integer(0), decodedResult.getLosses());
        assertEquals(dateFormed, decodedResult.getFormed());
        assertEquals(coach.getName(), decodedResult.getCoach().getName());
        assertEquals(coach.getAge(), decodedResult.getCoach().getAge());
        assertEquals(assistantCoach.getName(), decodedResult
                .getAssistantCoach().getName());
        assertEquals(assistantCoach.getAge(), decodedResult.getAssistantCoach()
                .getAge());
        assertEquals("testPlayer1", decodedResult.getPlayers().get(0).getName());
        assertEquals(new Integer(14), decodedResult.getPlayers().get(0)
                .getAge());
        assertEquals("testPlayer2", decodedResult.getPlayers().get(1).getName());
        assertEquals(new Integer(15), decodedResult.getPlayers().get(1)
                .getAge());
    }

    @Test
    public void testWithNulls() {
        // create test team
        ObjectId objectId = new ObjectId();
        Date dateFormed = new Date();
        Person coach = new Person("testCoach", new Integer(32));
        ArrayList<Person> players = new ArrayList<Person>();
        players.add(new Person("testPlayer1", new Integer(14)));
        players.add(new Person("testPlayer2", new Integer(15)));
        Team testTeam = new Team(objectId, "testTeamName", new Integer(2),
                new Integer(0), dateFormed, coach, null, players);

        // insert team
        collection.insertOne(testTeam);

        // query for inserted team
        Team decodedResult = collection.find(eq(Team.ID, objectId)).first();
        assertNotNull(decodedResult);

        // check result for correctness
        assertEquals(objectId, decodedResult.getId());
        assertEquals("testTeamName", decodedResult.getTeamName());
        assertEquals(new Integer(2), decodedResult.getWins());
        assertEquals(new Integer(0), decodedResult.getLosses());
        assertEquals(dateFormed, decodedResult.getFormed());
        assertEquals(coach.getName(), decodedResult.getCoach().getName());
        assertEquals(coach.getAge(), decodedResult.getCoach().getAge());

        assertNull(decodedResult.getAssistantCoach());

        assertEquals("testPlayer1", decodedResult.getPlayers().get(0).getName());
        assertEquals(new Integer(14), decodedResult.getPlayers().get(0)
                .getAge());
        assertEquals("testPlayer2", decodedResult.getPlayers().get(1).getName());
        assertEquals(new Integer(15), decodedResult.getPlayers().get(1)
                .getAge());
    }
}
