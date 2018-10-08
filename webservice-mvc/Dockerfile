FROM openjdk:10.0.2-jre
ADD build/libs/webservice-mvc-0.5.jar webservice-mvc-0.5.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","webservice-mvc-0.5.jar"]