FROM quay.io/ibmgaragecloud/gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble copyJarToServerJar --no-daemon

FROM registry.access.redhat.com/ubi8/openjdk-11:1.3-15

USER root

## comment the below line if there are no sec severities
RUN dnf -y update-minimal --security --sec-severity=Important --sec-severity=Critical && dnf clean all

COPY ./licenses /licenses

USER 1001

LABEL name="Java Spring App" \
      vendor="IBM" \
      version="v1.0.0" \
      release="1" \
      summary="This is an example of a container image." \
      description="This container image will deploy a Java Spring App"

COPY --from=builder /home/gradle/build/libs/server.jar ./server.jar

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]
