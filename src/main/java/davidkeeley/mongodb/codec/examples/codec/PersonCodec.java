package davidkeeley.mongodb.codec.examples.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import davidkeeley.mongodb.codec.examples.entity.Person;

public class PersonCodec implements Codec<Person> {

    public PersonCodec() {
    }

    public void encode(BsonWriter writer, Person value,
            EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeString(Person.NAME, value.getName());
        writer.writeInt32(Person.AGE, value.getAge());

        writer.writeEndDocument();
    }

    public Person decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String name = reader.readString(Person.NAME);
        Integer age = reader.readInt32(Person.AGE);

        reader.readEndDocument();
        return new Person(name, age);
    }

    public Class<Person> getEncoderClass() {
        return Person.class;
    }
}
