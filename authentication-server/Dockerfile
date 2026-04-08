FROM openjdk:11
ADD target/authentication-server.jar authentication-server.jar
ENTRYPOINT ["java", "-jar", "authentication-server.jar"]
EXPOSE 8087
# -------- Base Image --------
FROM eclipse-temurin:11-jre-alpine

# -------- Metadata (optional but recommended) --------
LABEL application="authentication-server"

# -------- App Directory --------
WORKDIR /app

# -------- Security: Non-root user --------
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# -------- Copy Artifact --------
COPY target/authentication-server.jar authentication-server.jar

# -------- Set Ownership --------
RUN chown appuser:appgroup authentication-server.jar

# -------- Switch User --------
USER appuser

# -------- Expose Port --------
EXPOSE 8087

# -------- JVM Options --------
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# -------- Health Check --------
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget -qO- http://localhost:8087/actuator/health || exit 1

# -------- Run Application --------
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar authentication-server.jar"]