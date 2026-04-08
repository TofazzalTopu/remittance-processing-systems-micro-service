# -------- Base Image --------
FROM eclipse-temurin:11-jre-alpine

# -------- Metadata (optional but recommended) --------
LABEL application="depertment-service"

# -------- App Directory --------
WORKDIR /app

# -------- Security: Non-root user --------
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# -------- Copy Artifact --------
COPY target/depertment-service.jar depertment-service.jar

# -------- Set Ownership --------
RUN chown appuser:appgroup depertment-service.jar

# -------- Switch User --------
USER appuser

# -------- Expose Port --------
EXPOSE 8082

# -------- JVM Options --------
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# -------- Health Check --------
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD wget -qO- http://localhost:8082/actuator/health || exit 1

# -------- Run Application --------
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar depertment-service.jar"]