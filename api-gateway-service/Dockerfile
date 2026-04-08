# -------- Base Image --------
FROM eclipse-temurin:11-jre-alpine

# -------- Metadata (optional but recommended) --------
LABEL application="api-gateway-service"

# -------- App Directory --------
WORKDIR /app

# -------- Security: Non-root user --------
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# -------- Copy Artifact --------
COPY target/api-gateway-service.jar api-gateway-service.jar

# -------- Set Ownership --------
RUN chown appuser:appgroup api-gateway-service.jar

# -------- Switch User --------
USER appuser

# -------- Expose Port --------
EXPOSE 8080

# -------- JVM Options --------
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# -------- Health Check --------
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# -------- Run Application --------
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar api-gateway-service.jar"]