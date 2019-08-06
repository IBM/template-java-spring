FROM registry.access.redhat.com/ubi8/ubi

RUN dnf install -y java-11-openjdk.x86_64

COPY ./build/libs/*.jar ./server.jar

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]
