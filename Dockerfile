FROM openjdk:11
COPY dataset.csv .
ADD /target/DataParserApi-1.0.0.jar DataParserApi.jar
ENTRYPOINT ["java", "-jar", "DataParserApi.jar"]