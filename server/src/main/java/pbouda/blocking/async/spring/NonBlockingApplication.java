package pbouda.blocking.async.spring;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

@SpringBootApplication
public class NonBlockingApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{NonBlockingApplication.class, MessageController.class}, args);
    }

    @RequestMapping
    public static class MessageController {

        private final AsyncRabbitTemplate rabbitTemplate;

        public MessageController(CachingConnectionFactory connectionFactory) {
            this.rabbitTemplate = new AsyncRabbitTemplate(new RabbitTemplate(connectionFactory));
            this.rabbitTemplate.start();
        }

        @GetMapping("invoke")
        public DeferredResult<ResponseEntity<String>> sendMessage() {
            var output = new DeferredResult<ResponseEntity<String>>();

            rabbitTemplate.sendAndReceive("uppercase", null, Utils.request())
                    .completable()
                    .thenApply(Message::getBody)
                    .thenApply(String::new)
                    .thenAccept(message -> output.setResult(ResponseEntity.ok(message)));

            return output;
        }
    }
}
