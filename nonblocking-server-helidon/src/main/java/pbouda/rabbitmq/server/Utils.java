package pbouda.rabbitmq.server;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import java.util.UUID;

final class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    static Message request() {
        Lorem LOREM = LoremIpsum.getInstance();
        String name = LOREM.getFirstName() + " " + LOREM.getLastName();

        UUID messageID = UUID.randomUUID();
        LOG.info("We are sending name " + name + " with ID: " + messageID.toString());

        return new Message(name.getBytes(), new MessageProperties());
    }

    static CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses("localhost:5672");
        factory.setUsername("admin");
        factory.setPassword("admin");
        return factory;
    }
}
