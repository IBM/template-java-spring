FROM quay.io/ibmgaragecloud/gradle:jdk11 AS builder

WORKDIR /home/gradle
COPY . .
RUN ./gradlew assemble copyJarToServerJar --no-daemon

## Requirement 1: Universal base image (UBI)
## Requirement 4: Non-root, arbitrary user IDs is already taken care
## Requirement 5: Two-stage image builds is already taken care

FROM registry.access.redhat.com/ubi8/openjdk-11:1.3-10

## Requirement 2: Updated image security content
USER root

## comment the below line if there are no sec severities
RUN dnf -y update-minimal --security --sec-severity=Important --sec-severity=Critical && dnf clean all

## Requirement 7: Image License
COPY ./licenses /licenses

USER default

## Requirement 3: Do not modify, replace or combine Red Hat packages or layers is already taken care

## Requirement 6: Image Identification
ARG NAME="vendorx/vendorX"
ARG VENDOR="VendorX"
ARG VERSION="1.0"
ARG RELEASE="1"
ARG SUMMARY="Red Hat VendorX Summary"
ARG DESCRIPTION="Red Hat VendorX Description"

LABEL name=${NAME} \
      vendor=${VENDOR} \
      version=${VERSION} \
      release=${RELEASE} \
      summary=${SUMMARY} \
      description=${DESCRIPTION}

COPY --from=builder /home/gradle/build/libs/server.jar ./server.jar

EXPOSE 9080/tcp

CMD ["java", "-jar", "./server.jar"]
