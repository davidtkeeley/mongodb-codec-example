package davidkeeley.mongodb.codec.examples.provider;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import davidkeeley.mongodb.codec.examples.codec.PersonCodec;
import davidkeeley.mongodb.codec.examples.entity.Person;

public class PersonCodecProvider implements CodecProvider {

    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> type, CodecRegistry codecRegistry) {
        if (type == Person.class) {
            return (Codec<T>) new PersonCodec();
        }
        return null;
    }
}
