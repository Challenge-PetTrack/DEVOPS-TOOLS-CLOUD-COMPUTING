# ==============================================================================
# Imagem Oficial MySQL 8.0 para o Banco de Dados PetTrack
# ==============================================================================
FROM mysql:8.0

# Variáveis padrão de ambiente para inicialização
ENV MYSQL_DATABASE=pettrack
ENV MYSQL_USER=pettrack_user
ENV MYSQL_PASSWORD=PetTrack@2026Secure
ENV MYSQL_ROOT_PASSWORD=Root@2026Secure

# Cópia do script de inicialização do banco (DDL + Inserts)
COPY docker-entrypoint-initdb.d/init.sql /docker-entrypoint-initdb.d/init.sql

EXPOSE 3306
