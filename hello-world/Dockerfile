FROM openjdk:8u322-jre-slim-buster

COPY target/hello-world-1.0-SNAPSHOT.jar /hello-world.jar

CMD java -server -Xms1M -Xmx1m -jar /hello-world.jar