FROM maven:3.6.2-jdk-8

COPY pom.xml /tmp/
COPY src /tmp/src/

WORKDIR /tmp/
RUN mvn -DSERVICE_HOST=localhost -DSERVICE_PORT=8080 install
EXPOSE 8080

CMD ["mvn", "spring-boot:run"]