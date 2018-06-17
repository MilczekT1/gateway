FROM openjdk:8u151-jdk-stretch
#VOLUME /tmp
ADD build/libs/webservice-mvc-0.1.jar webservice-mvc-0.1.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","webservice-mvc-0.1.jar"]
#docker build -t konradboniecki/budget:mail-1.0 . && docker push konradboniecki/budget:mail-1.0