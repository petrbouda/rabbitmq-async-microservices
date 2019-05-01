# RabbitMQ's Async/Non-blocking Microservices

```
# Start RabbitMQ
docker-compose up

# Start Service which processes a published event with 0-500ms delay
java -jar service/target/service.jar

# Start one of the Servers
java -jar blocking-server-spring/target/blocking-server-spring.jar
java -jar nonblocking-server-spring/target/nonblocking-server-spring.jar
java -jar nonblocking-server-helidon/target/nonblocking-server-helidon.jar

# Run Performance Test
mvn gatling:test -Dgatling.simulationClass=pbouda.rabbitmq.gatling.Generator
```