# -------- Base Image --------
FROM eclipse-temurin:11-jre-alpine

# -------- Metadata (optional but recommended) --------
LABEL application="cloud-config-server"

# -------- App Directory --------
WORKDIR /app

# -------- Security: Non-root user --------
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# -------- Copy Artifact --------
COPY target/cloud-config-server.jar cloud-config-server.jar

# -------- Set Ownership --------
RUN chown appuser:appgroup cloud-config-server.jar

# -------- Switch User --------
USER appuser

# -------- Expose Port --------
EXPOSE 9092

# -------- JVM Options --------
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# -------- Health Check --------
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget -qO- http://localhost:9092/actuator/health || exit 1

# -------- Run Application --------
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar cloud-config-server.jar"]