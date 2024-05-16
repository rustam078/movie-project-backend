FROM openjdk:17
WORKDIR /opt
ENV PORT 8080
EXPOSE 8080
COPY target/movie.jar /opt/movie.jar
ENTRYPOINT exec java $JAVA_OPTS -jar movie.jar
