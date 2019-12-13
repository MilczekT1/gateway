FROM openjdk:11.0.3-jdk-stretch
ADD /target/gateway-0.5.0.jar app.jar
HEALTHCHECK --start-period=15s --interval=10s --timeout=5s --retries=10 CMD curl -f http://localhost:8080/actuator/health
ENTRYPOINT ["java", "-jar", \
    "-Djava.security.egd=file:/dev/./urandom ", "app.jar" \
]
