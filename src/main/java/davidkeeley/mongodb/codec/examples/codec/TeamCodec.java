package davidkeeley.mongodb.codec.examples.codec;

import java.util.ArrayList;
import java.util.Date;

import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import davidkeeley.mongodb.codec.examples.entity.Team;
import davidkeeley.mongodb.codec.examples.entity.Person;

public class TeamCodec implements CollectibleCodec<Team> {
    private CodecRegistry codecRegistry;

    public TeamCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    public void encode(BsonWriter writer, Team value,
            EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeObjectId(Team.ID, value.getId());
        writer.writeString(Team.TEAM_NAME, value.getTeamName());
        writer.writeInt32(Team.WINS, value.getWins());
        writer.writeInt32(Team.LOSSES, value.getLosses());
        writer.writeDateTime(Team.FORMED, value.getFormed().getTime());

        writer.writeName(Team.COACH);
        encoderContext.encodeWithChildContext(codecRegistry.get(Person.class),
                writer, value.getCoach());

        if (value.getAssistantCoach() != null) {
            writer.writeName(Team.ASSISTANT_COACH);
            encoderContext.encodeWithChildContext(
                    codecRegistry.get(Person.class), writer,
                    value.getAssistantCoach());
        } else {
            writer.writeNull(Team.ASSISTANT_COACH);
        }

        writer.writeStartArray(Team.PLAYERS);
        for (Person player : value.getPlayers()) {
            encoderContext.encodeWithChildContext(
                    codecRegistry.get(Person.class), writer, player);
        }
        writer.writeEndArray();

        writer.writeEndDocument();
    }

    public Class<Team> getEncoderClass() {
        return Team.class;
    }

    public Team decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        ObjectId _id = reader.readObjectId(Team.ID);
        String teamName = reader.readString(Team.TEAM_NAME);
        Integer wins = reader.readInt32(Team.WINS);
        Integer losses = reader.readInt32(Team.LOSSES);
        Date formed = new Date(reader.readDateTime(Team.FORMED));

        reader.readName(Team.COACH);
        Person coach = codecRegistry.get(Person.class).decode(reader,
                decoderContext);

        reader.readName(Team.ASSISTANT_COACH);
        Person assistantCoach = null;
        if (reader.getCurrentBsonType() == BsonType.DOCUMENT) {
            assistantCoach = codecRegistry.get(Person.class).decode(reader,
                    decoderContext);
        } else if (reader.getCurrentBsonType() == BsonType.NULL) {
            reader.readNull();
        } else {
            // Panic or throw some exception
        }

        ArrayList<Person> players = new ArrayList<Person>();
        reader.readName(Team.PLAYERS);
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            players.add(codecRegistry.get(Person.class).decode(reader,
                    decoderContext));
        }
        reader.readEndArray();

        return new Team(_id, teamName, wins, losses, formed, coach,
                assistantCoach, players);
    }

    public Team generateIdIfAbsentFromDocument(Team document) {
        if (!documentHasId(document)) {
            document.generateId();
        }
        return document;
    }

    public boolean documentHasId(Team document) {
        if (document.getId() == null) {
            return false;
        }
        return true;
    }

    public BsonValue getDocumentId(Team document) {
        if (!documentHasId(document)) {
            throw new IllegalStateException("Document does not have id");
        }
        return new BsonString(document.getId().toHexString());
    }

}
