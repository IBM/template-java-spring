FROM quay.io/ibmgaragecloud/gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble copyJarToServerJar --no-daemon

## Requirement 1: Universal base image (UBI)
## Requirement 4: Non-root, arbitrary user IDs is already taken care
## Requirement 5: Two-stage image builds is already taken care

FROM registry.access.redhat.com/ubi8/openjdk-11:1.3-11

## Requirement 2: Updated image security content
USER root

## comment the below line if there are no sec severities
RUN dnf -y update-minimal --security --sec-severity=Important --sec-severity=Critical && dnf clean all

## Requirement 7: Image License
COPY ./licenses /licenses

USER 1001

## Requirement 3: Do not modify, replace or combine Red Hat packages or layers is already taken care

## Requirement 6: Image Identification
LABEL name="Java Spring App" \
      vendor="IBM" \
      version="v1.0.0" \
      release="1" \
      summary="This is an example of a container image." \
      description="This container image will deploy a Java Spring App"

COPY --from=builder /home/gradle/build/libs/server.jar ./server.jar

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]
