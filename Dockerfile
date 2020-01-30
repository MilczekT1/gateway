FROM openjdk:11.0.3-jdk-stretch
HEALTHCHECK --start-period=10s --interval=10s --timeout=5s --retries=10 CMD curl -f http://localhost:8080/actuator/health
ADD /target/gateway-*.jar app.jar
ENTRYPOINT ["java", "-jar", \
    "-Djava.security.egd=file:/dev/./urandom ", "app.jar" \
]
