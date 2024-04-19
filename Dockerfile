ARG JAR_NAME

FROM amazoncorretto:17-alpine as corretto-jdk
ARG JAR_NAME
COPY ./$JAR_NAME.jar /app/app.jar
RUN mkdir /app/unpacked && \
    cd /app/unpacked && \
    unzip ../app.jar && \
    cd .. && \
    $JAVA_HOME/bin/jdeps \
    --ignore-missing-deps \
    --print-module-deps \
    -q \
    --recursive \
    --multi-release 17 \
    --class-path="./unpacked/BOOT-INF/lib/*" \
    --module-path="./unpacked/BOOT-INF/lib/*" \
    ./app.jar > /deps.info

RUN apk add --no-cache binutils
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules $(cat /deps.info) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /jre

FROM alpine:3.19.1
EXPOSE 8080
WORKDIR /
ARG JAR_NAME
ENV JAR_FILE="$JAR_NAME.jar"
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY ./$JAR_NAME.jar /$JAR_NAME.jar
COPY --from=corretto-jdk /jre $JAVA_HOME

CMD [ "/bin/sh", "-c", "/jre/bin/java -jar $JAR_FILE" ]