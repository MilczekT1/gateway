FROM openjdk:8u171-jre-alpine3.8
ADD build/libs/webservice-mvc-0.5.jar webservice-mvc-0.5.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","webservice-mvc-0.5.jar"]