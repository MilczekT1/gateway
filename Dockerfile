FROM konradboniecki/budget:java-base-image-latest
HEALTHCHECK --start-period=10s --interval=10s --timeout=5s --retries=10 CMD curl -f https://localhost:8443/actuator/health -k
ADD /target/gateway-*.jar app.jar
ENTRYPOINT ["java", "-jar", \
    "-Djava.security.egd=file:/dev/./urandom", "app.jar" \
]
