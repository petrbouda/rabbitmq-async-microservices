package pbouda.rabbitmq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    public static class MessageListener {
        public String handleMessage(String message) {
            Random rand = new Random();
            // Obtain a number between [0 - 49] + 50 = [50 - 99]
            int n = rand.nextInt(50) + 50;

            try {
                Thread.sleep(n);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return message.toUpperCase();
        }
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses("localhost:5672");
        factory.setUsername("admin");
        factory.setPassword("admin");
        return factory;
    }

    @Bean
    public SimpleMessageListenerContainer serviceListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setConcurrentConsumers(20);
        container.setMaxConcurrentConsumers(40);
        container.setQueueNames("uppercase_messages");
        container.setMessageListener(new MessageListenerAdapter(new MessageListener(), new MessageConverter()));
        return container;
    }

    public class MessageConverter extends Jackson2JsonMessageConverter {
        public MessageConverter() {
            super(new ObjectMapper());
        }

        @Override
        public String fromMessage(Message message) throws MessageConversionException {
            return new String(message.getBody());
        }
    }
}