FROM quay.io/ibmgaragecloud/gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble copyJarToServerJar --no-daemon

# Requirement 1 Universal base image (UBI)
FROM registry.access.redhat.com/ubi8/ubi:8.2

# Requirement 2 Updated image security content
USER root
RUN dnf -y update-minimal --security --sec-severity=Important --sec-severity=Critical
# USER default

# Requirement 3, 4 & 5 is already taken care

# Requirement 6 Image Identification
LABEL name="test/certification-test" \
      maintainer="maintainer@test.com" \
      vendor="Tester" \
      version="3.5" \
      release="1" \
      summary="These labels are appended to pass the Red Hat certification" \
      description="Image Identification clause of the certification"

RUN dnf install -y java-11-openjdk.x86_64

COPY --from=builder /home/gradle/build/libs/server.jar ./server.jar

# Requirement 7 Image License
RUN mkdir /licenses
COPY LICENSE.txt /licenses

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]
