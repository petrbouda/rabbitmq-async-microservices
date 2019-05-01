package pbouda.rabbitmq.server;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

final class Utils {

    static Message request() {
        Lorem LOREM = LoremIpsum.getInstance();
        String name = LOREM.getFirstName() + " " + LOREM.getLastName();
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
