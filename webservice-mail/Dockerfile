FROM openjdk:8u171-jre-alpine3.8
ADD build/libs/webservice-mail-0.5.jar webservice-mail-0.5.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","webservice-mail-0.5.jar"]
#docker build -t konradboniecki/budget:mail-0.5 . && docker push konradboniecki/budget:mail-0.5