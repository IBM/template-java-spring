FROM gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble --no-daemon  

FROM registry.access.redhat.com/ubi8/ubi

RUN dnf install -y java-11-openjdk.x86_64

COPY --from=builder /home/gradle/build/libs/*.jar ./server.jar

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]