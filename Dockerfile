FROM quay.io/ibmgaragecloud/gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble copyJarToServerJar --no-daemon

# Requirement 1: Universal base image (UBI)
# Requirement 4: Non-root, arbitrary user IDs is already taken care
# Requirement 5: Two-stage image builds is already taken care

FROM registry.access.redhat.com/ubi8/openjdk-11

# Requirement 2: Updated image security content
USER root
RUN dnf -y update-minimal --security --sec-severity=Important --sec-severity=Critical && dnf clean all
USER default

# Requirement 3: Do not modify, replace or combine Red Hat packages or layers is already taken care

# Requirement 6: Image Identification
ARG NAME
ARG VENDOR
ARG VERSION
ARG RESEASE
ARG SUMMARY
ARG DESCRIPTION

LABEL name=${NAME} \
      vendor=${VENDOR} \
      version=${VERSION} \
      release=${RESEASE} \
      summary=${SUMMARY} \
      description=${DESCRIPTION}

COPY --from=builder /home/gradle/build/libs/server.jar ./server.jar

# Requirement 7: Image License
COPY ./licenses ./licenses

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]