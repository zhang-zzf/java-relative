FROM amazoncorretto:17
WORKDIR /iot-card
COPY application/target/application-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD java -server -Xms384m -Xmx384m -jar app.jar &>/dev/null