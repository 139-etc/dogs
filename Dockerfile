FROM eclipse-temurin:8-jdk

WORKDIR /dogs

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

CMD ["sh","-c","exec java -Dserver.port=${PORT:-10000} -jar target/*.jar --debug 2>&1"]
