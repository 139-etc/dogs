FROM eclipse-temurin:8-jdk

WORKDIR /dogs

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

CMD ["sh", "-c", "echo '=== PORT CHECK ===' && echo PORT=$PORT && echo '=== START JAVA ===' && timeout 30 java -Dserver.port=$PORT -jar target/*.jar; echo '=== JAVA EXIT CODE:' $? '==='"]