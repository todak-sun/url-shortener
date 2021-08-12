FROM openjdk:11-jdk

COPY ./target/shortenURL-0.0.1-SNAPSHOT.jar /usr/local/app/app.jar

WORKDIR /usr/local/app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]