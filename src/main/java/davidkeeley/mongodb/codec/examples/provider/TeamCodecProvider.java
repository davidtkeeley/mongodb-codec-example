package davidkeeley.mongodb.codec.examples.provider;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import davidkeeley.mongodb.codec.examples.codec.TeamCodec;
import davidkeeley.mongodb.codec.examples.entity.Team;

public class TeamCodecProvider implements CodecProvider {

    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> type, CodecRegistry codecRegistry) {
        if (type == Team.class) {
            return (Codec<T>) new TeamCodec(codecRegistry);
        }
        return null;
    }
}
