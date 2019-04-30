package pbouda.blocking.async.spring;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class BlockingApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{BlockingApplication.class, MessageController.class}, args);
    }

    @RequestMapping
    public static class MessageController {

        private final RabbitTemplate rabbitTemplate;

        public MessageController(CachingConnectionFactory connectionFactory) {
            this.rabbitTemplate = new RabbitTemplate(connectionFactory);
        }

        @GetMapping("invoke")
        public String sendMessage() {
            Message response = rabbitTemplate.sendAndReceive("uppercase", null, Utils.request());
            return new String(response.getBody());
        }
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        return Utils.connectionFactory();
    }
}
