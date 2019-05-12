# RabbitMQ's Async/Non-blocking Microservices

```
# Start RabbitMQ
docker-compose up

# Start Service which processes a published event with 0-500ms delay
docker run -d -m=500m --cpus="1" --network host rabbitmq-service

# Start one of the Servers
java -jar blocking-server-spring/target/blocking-server-spring.jar
java -jar nonblocking-server-spring/target/nonblocking-server-spring.jar
java -jar nonblocking-server-helidon/target/nonblocking-server-helidon.jar

# Run Performance Test
mvn gatling:test -Dgatling.simulationClass=pbouda.rabbitmq.gatling.Generator
```

### Enable Native Memory Tracking

```
-XX:+UnlockDiagnosticVMOptions -XX:NativeMemoryTracking=summary -XX:+PrintNMTStatistics
```

### Enable Async-Profiler

https://github.com/petrbouda/async-profiler-playground

```
(context-switches / lock / cache-misses / cycles / instructions / cpu / alloc)
profiler.sh -d 30 -e lock -f profile.svg <pid>
```

### Example 

```
java -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+PreserveFramePointer \
-jar blocking-server-spring/target/blocking-server-spring.jar

java -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+PreserveFramePointer \
-jar nonblocking-server-spring/target/nonblocking-server-spring.jar

java -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -XX:+PreserveFramePointer \
-jar nonblocking-server-helidon/target/nonblocking-server-helidon.jar
```