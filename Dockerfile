FROM eclipse-temurin:8-jdk

WORKDIR /dogs

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

CMD ["sh", "-c", "echo '=== JAVA START ==='; java -version; echo '=== JAR START ==='; java -Dserver.port=${PORT} -jar target/*.jar; CODE=$?; echo \"=== JAVA EXIT CODE: $CODE ===\"; sleep 60"]