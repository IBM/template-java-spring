FROM quay.io/ibmgaragecloud/gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble copyJarToServerJar --no-daemon

FROM registry.access.redhat.com/ubi8/openjdk-11:1.3-15

USER root

## Uncomment the below line to update image security content if any
# RUN dnf -y update-minimal --security --sec-severity=Important --sec-severity=Critical && dnf clean all

COPY ./licenses /licenses

USER 1001

LABEL name="ibm/template-java-spring" \
      vendor="IBM" \
      version="1.3" \
      release="15" \
      summary="This is an example of a container image." \
      description="This container image will deploy a Java Spring App"

COPY --from=builder /home/gradle/build/libs/server.jar ./server.jar

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]
