package davidkeeley.mongodb.codec.examples.entity;

import java.util.ArrayList;
import java.util.Date;

import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class Team implements Bson {
    public static final String ID = "_id";
    public static final String TEAM_NAME = "teamName";
    public static final String WINS = "wins";
    public static final String LOSSES = "losses";
    public static final String FORMED = "formed";
    public static final String COACH = "coach";
    public static final String ASSISTANT_COACH = "assistantCoach";
    public static final String PLAYERS = "players";

    private ObjectId _id;
    private String teamName;
    private Integer wins;
    private Integer losses;
    private Date formed;
    private Person coach;
    private Person assistantCoach;
    private ArrayList<Person> players;

    public Team() {

    }

    public Team(String teamName, Integer wins, Integer losses, Date formed,
            Person coach, Person assistantCoach, ArrayList<Person> players) {
        if (teamName == null || wins == null || losses == null
                || formed == null || coach == null || players == null) {
            throw new IllegalArgumentException("fields cant be null");
        }
        this.teamName = teamName;
        this.wins = wins;
        this.losses = losses;
        this.formed = formed;
        this.coach = coach;
        this.assistantCoach = assistantCoach;
        this.players = players;
    }

    public Team(ObjectId _id, String teamName, Integer wins, Integer losses,
            Date formed, Person coach, Person assistantCoach,
            ArrayList<Person> players) {
        this(teamName, wins, losses, formed, coach, assistantCoach, players);
        this._id = _id;
    }

    public ObjectId getId() {
        return this._id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId generateId() {
        if (this._id == null) {
            _id = new ObjectId();
        }
        return _id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Integer getWins() {
        return wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public Date getFormed() {
        return formed;
    }

    public Person getCoach() {
        return coach;
    }

    public Person getAssistantCoach() {
        return assistantCoach;
    }

    public ArrayList<Person> getPlayers() {
        return players;
    }

    public <TDocument> BsonDocument toBsonDocument(
            Class<TDocument> documentClass, CodecRegistry codecRegistry) {
        return new BsonDocumentWrapper<Team>(this,
                codecRegistry.get(Team.class));
    }

}
