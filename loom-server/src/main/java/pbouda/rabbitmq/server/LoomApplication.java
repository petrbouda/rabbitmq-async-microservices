package pbouda.rabbitmq.server;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.glassfish.jersey.jetty.JettyHttpContainer;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoomApplication {

    public static void main(String[] args) throws Exception {
        try {
            ResourceConfig application = new ResourceConfig(MessageController.class);
            startServer(8080, ContainerFactory.createContainer(JettyHttpContainer.class, application));

            System.out.println("Application started: http://localhost:8080/");
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            Logger.getLogger(LoomApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Path("invoke")
    public static class MessageController {

        private final RabbitTemplate rabbitTemplate
                = new RabbitTemplate(Utils.connectionFactory());

        @GET
        public String sendMessage() {
            Message response = rabbitTemplate.sendAndReceive("uppercase", null, Utils.request());
            return new String(response.getBody());
        }
    }

    private static void startServer(int port, JettyHttpContainer handler) throws Exception {
        Server server = new Server(fiberThreadPool());
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(new HttpConfiguration()));

        http.setPort(port);
        server.setConnectors(new Connector[]{http});
        server.setHandler(handler);
        server.start();
    }

    private static ThreadPool fiberThreadPool() {
        return new ThreadPool() {
            @Override
            public void join() {
            }

            @Override
            public int getThreads() {
                return 0;
            }

            @Override
            public int getIdleThreads() {
                return 0;
            }

            @Override
            public boolean isLowOnThreads() {
                return false;
            }

            @Override
            public void execute(Runnable command) {
                Fiber.schedule(command);
            }
        };
    }
}
