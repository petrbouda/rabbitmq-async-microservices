package pbouda.rabbitmq.service;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    public static class MessageListener {

        public String handleMessage(byte[] message) {
            int sleep = (int) (Math.random() * 500);

            String content = new String(message);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return content.toUpperCase();
        }
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        var factory = new CachingConnectionFactory();
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
        container.setMessageListener(new MessageListenerAdapter(new MessageListener()));
        return container;
    }

}