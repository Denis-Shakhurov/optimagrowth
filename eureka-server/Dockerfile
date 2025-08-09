FROM bellsoft/liberica-openjre-alpine:22-cds AS layers
WORKDIR /application
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]