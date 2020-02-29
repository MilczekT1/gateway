FROM konradboniecki/budget:base-image-286
HEALTHCHECK --start-period=10s --interval=10s --timeout=5s --retries=10 CMD curl -f https://localhost:8080/actuator/health -k
ADD /target/gateway-*.jar app.jar
ENTRYPOINT ["java", "-jar", \
    "-Djava.security.egd=file:/dev/./urandom -Djavax.net.debug=ssl:all -Davax.net.ssl.trustStore=baeldung.p12 -Djavax.net.ssl.trustStorePassword=dupa123", "app.jar" \
]
