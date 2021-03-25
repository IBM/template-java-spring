FROM quay.io/ibmgaragecloud/gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble copyJarToServerJar --no-daemon

# Requirement 1: Universal base image (UBI)
FROM registry.access.redhat.com/ubi8/ubi:8.2

# Requirement 2: Updated image security content
USER root
RUN dnf -y update-minimal --security --sec-severity=Important --sec-severity=Critical
# USER default

# Requirement 3: Do not modify, replace or combine Red Hat packages or layers is already taken care
# Requirement 4: Non-root, arbitrary user IDs is already taken care
# Requirement 5: Two-stage image builds is already taken care

# Requirement 6: Image Identification
ARG NAME
ARG MAINTAINER
ARG VENDOR
ARG VERSION
ARG RESEASE
ARG SUMMARY
ARG DESCRIPTION

LABEL name=${NAME} \
      maintainer=${MAINTAINER} \
      vendor=${VENDOR} \
      version=${VERSION} \
      release=${RESEASE} \
      summary=${SUMMARY} \
      description=${DESCRIPTION}

RUN dnf install -y java-11-openjdk.x86_64

COPY --from=builder /home/gradle/build/libs/server.jar ./server.jar

# Requirement 7: Image License
RUN mkdir /licenses
COPY LICENSE.txt /licenses

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]
