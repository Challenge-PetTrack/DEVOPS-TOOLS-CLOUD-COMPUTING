# ==============================================================================
# ESTÁGIO 1: Build da Aplicação Java com Maven
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ==============================================================================
# ESTÁGIO 2: Imagem de Execução Enxuta com Usuário NÃO-ROOT
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Criação de grupo e usuário sem privilégios administrativos (Requisito 8.2)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia o JAR compilado do estágio anterior e ajusta permissões
COPY --from=builder /build/target/*.jar app.jar
RUN chown appuser:appgroup app.jar

# Define o usuário não-root
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
