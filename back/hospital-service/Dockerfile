FROM openjdk:17-oracle
VOLUME /tmp
COPY target/*.jar  service.jar
ENTRYPOINT ["java","-jar", "service.jar"]
