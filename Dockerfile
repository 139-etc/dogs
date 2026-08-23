FROM eclipse-temurin:8-jdk

WORKDIR /dogs

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

CMD ["sh", "-c", "echo '=== JAR START ==='; java -Dserver.port=${PORT} -Ddebug=true -Dlogging.level.root=DEBUG -Dlogging.level.com.zaxxer.hikari=DEBUG -Dlogging.level.org.springframework.jdbc=DEBUG -jar target/*.jar 2>&1; CODE=$?; echo \"=== JAVA EXIT CODE: $CODE ===\"; sleep 120"]