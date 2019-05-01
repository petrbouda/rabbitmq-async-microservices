package pbouda.rabbitmq.server;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public final class HelidonApplication {

    private static Routing createRouting(AsyncRabbitTemplate rabbitTemplate) {
        return Routing.builder()
                .get("/invoke", (req, resp) ->
                        rabbitTemplate.sendAndReceive("uppercase", null, Utils.request())
                                .completable()
                                .thenApply(Message::getBody)
                                .thenApply(String::new)
                                .thenAccept(resp::send))
                .build();
    }

    public static void main(final String[] args) {
        ServerConfiguration serverConfig =
                ServerConfiguration.builder()
                        .port(8080)
                        .build();

        var rabbitTemplate = new AsyncRabbitTemplate(new RabbitTemplate(Utils.connectionFactory()));
        rabbitTemplate.start();

        WebServer.create(serverConfig, createRouting(rabbitTemplate))
                .start()
                .thenAccept(server -> System.out.println("Started on port: " + server.port()));
    }
}
