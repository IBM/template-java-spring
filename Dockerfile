FROM registry.access.redhat.com/ubi9/openjdk-17:1.14-2.1681917140 as builder

COPY --chown=default . .

RUN ./gradlew copyJarToServerJar --no-daemon && ls build/libs

FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:1.14-2.1681917142

ARG NAME="ibm/template-java-spring"
ARG SUMMARY="This is an example of a container image."
ARG DESCRIPTION="This container image will deploy a Java Spring App"
ARG VENDOR="IBM"
ARG VERSION="1.3"
ARG RELEASE="15"

## https://connect.redhat.com/en/partner-resources/container-certification-policy-guide--archived
LABEL name=$NAME \
      vendor=$VENDOR \
      version=$VERSION \
      release=$RELEASE \
      summary=$SUMMARY \
      description=$DESCRIPTION

COPY licenses /licenses

COPY --from=builder --chown=default /home/default/build/libs/server.jar .

CMD ["java", "-jar", "server.jar"]
