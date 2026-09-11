#!/bin/bash
set -e

echo "=========================================================="
echo "   🐾 PROJETO PETTRACK - CLYVO VET | FIAP CHALLENGE 2026"
echo "   DEPLOY AUTOMATIZADO VIA AZURE CLI (ACR + ACI + STORAGE)"
echo "   Disciplina: DevOps Tools & Cloud Computing - Sprint 3"
echo "=========================================================="

# 1. CONFIGURAÇÃO DE VARIÁVEIS PRINCIPAIS
RM="563719"
LOCATION="eastus"
RESOURCE_GROUP="rg-pettrack-${RM}"
ACR_NAME="acrpettrack${RM}"
FILE_SHARE_NAME="pettrack-db-data"
DB_ACI_NAME="pettrack-db"
APP_ACI_NAME="pettrack-app"
DNS_DB="pettrack-db-${RM}"
DNS_APP="pettrack-app-${RM}"

DB_USER="pettrack_user"
DB_PASS="PetTrack@2026Secure"
DB_ROOT_PASS="Root@2026Secure"
DB_NAME="pettrack"

# 2. BUILD LOCAL DAS IMAGENS DOCKER (LINUX/AMD64)
echo ""
echo "--> [1/6] Build das imagens Docker para arquitetura linux/amd64..."
docker build --platform linux/amd64 -t pettrack-app:latest ./app
docker build --platform linux/amd64 -t pettrack-db:latest -f ./db/Dockerfile ./db

# 3. CRIAÇÃO DO RESOURCE GROUP
echo ""
echo "--> [2/6] Garantindo Resource Group: $RESOURCE_GROUP em $LOCATION..."
az group create --name "$RESOURCE_GROUP" --location "$LOCATION" --tags owner=pettrack environment=production

# 4. CRIAÇÃO DO AZURE CONTAINER REGISTRY (ACR)
echo ""
echo "--> [3/6] Verificando e criando Azure Container Registry: $ACR_NAME..."
AVAILABILITY=$(az acr check-name --name "$ACR_NAME" --query nameAvailable -o tsv)

if [ "$AVAILABILITY" = "true" ]; then
    echo "Criando ACR $ACR_NAME..."
    az acr create --resource-group "$RESOURCE_GROUP" --name "$ACR_NAME" --sku Basic --admin-enabled true
else
    echo "ACR $ACR_NAME já existente. Garantindo admin habilitado..."
    az acr update --name "$ACR_NAME" --resource-group "$RESOURCE_GROUP" --admin-enabled true || true
fi

ACR_LOGIN_SERVER=$(az acr show --name "$ACR_NAME" --query loginServer -o tsv)
ACR_USERNAME=$(az acr credential show --name "$ACR_NAME" --query username -o tsv)
ACR_PASSWORD=$(az acr credential show --name "$ACR_NAME" --query 'passwords[0].value' -o tsv)

echo "ACR pronto: $ACR_LOGIN_SERVER"

# 5. PUSH DAS IMAGENS PARA O REGISTRY
echo ""
echo "--> [4/6] Autenticando no ACR e enviando as imagens..."
docker login "$ACR_LOGIN_SERVER" -u "$ACR_USERNAME" -p "$ACR_PASSWORD"

docker tag pettrack-db:latest "$ACR_LOGIN_SERVER/pettrack-db:latest"
docker tag pettrack-app:latest "$ACR_LOGIN_SERVER/pettrack-app:latest"

docker push "$ACR_LOGIN_SERVER/pettrack-db:latest"
docker push "$ACR_LOGIN_SERVER/pettrack-app:latest"

# 6. CONFIGURAÇÃO DO STORAGE ACCOUNT E FILE SHARE (PERSISTÊNCIA)
echo ""
echo "--> [5/6] Configurando Azure Storage Account para persistência de dados..."
STORAGE_ACCOUNT=$(az storage account list --resource-group "$RESOURCE_GROUP" --query '[0].name' -o tsv)

if [ -z "$STORAGE_ACCOUNT" ]; then
  STORAGE_ACCOUNT="stpettrack${RM}"
  echo "Criando nova Storage Account: $STORAGE_ACCOUNT..."
  az storage account create \
    --resource-group "$RESOURCE_GROUP" \
    --name "$STORAGE_ACCOUNT" \
    --location "$LOCATION" \
    --sku Standard_LRS
else
  echo "Reaproveitando Storage Account: $STORAGE_ACCOUNT"
fi

STORAGE_KEY=$(az storage account keys list --resource-group "$RESOURCE_GROUP" --account-name "$STORAGE_ACCOUNT" --query '[0].value' -o tsv)

az storage share create \
  --account-name "$STORAGE_ACCOUNT" \
  --account-key "$STORAGE_KEY" \
  --name "$FILE_SHARE_NAME" || true

# 7. DEPLOY DO ACI - BANCO DE DADOS (MYSQL COM PERSISTÊNCIA)
echo ""
echo "--> [6/6] Criando Azure Container Instance do Banco de Dados: $DB_ACI_NAME..."
az container delete --resource-group "$RESOURCE_GROUP" --name "$DB_ACI_NAME" --yes || true

az container create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$DB_ACI_NAME" \
  --image "$ACR_LOGIN_SERVER/pettrack-db:latest" \
  --os-type Linux \
  --restart-policy Always \
  --registry-login-server "$ACR_LOGIN_SERVER" \
  --registry-username "$ACR_USERNAME" \
  --registry-password "$ACR_PASSWORD" \
  --dns-name-label "$DNS_DB" \
  --ports 3306 \
  --cpu 1 \
  --memory 1.5 \
  --environment-variables \
      MYSQL_ROOT_PASSWORD="$DB_ROOT_PASS" \
      MYSQL_DATABASE="$DB_NAME" \
      MYSQL_USER="$DB_USER" \
      MYSQL_PASSWORD="$DB_PASS" \
  --azure-file-volume-account-name "$STORAGE_ACCOUNT" \
  --azure-file-volume-account-key "$STORAGE_KEY" \
  --azure-file-volume-share-name "$FILE_SHARE_NAME" \
  --azure-file-volume-mount-path /var/lib/mysql

echo "Aguardando 25 segundos para estabilização do banco de dados..."
sleep 25

DB_HOST=$(az container show --resource-group "$RESOURCE_GROUP" --name "$DB_ACI_NAME" --query ipAddress.fqdn -o tsv)
echo "Banco disponível em: $DB_HOST:3306"

# 8. DEPLOY DO ACI - APLICAÇÃO (SPRING BOOT SEM PRIVILÉGIOS ROOT)
echo ""
echo "--> Criando Azure Container Instance da Aplicação: $APP_ACI_NAME..."
az container delete --resource-group "$RESOURCE_GROUP" --name "$APP_ACI_NAME" --yes || true

az container create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$APP_ACI_NAME" \
  --image "$ACR_LOGIN_SERVER/pettrack-app:latest" \
  --os-type Linux \
  --restart-policy Always \
  --registry-login-server "$ACR_LOGIN_SERVER" \
  --registry-username "$ACR_USERNAME" \
  --registry-password "$ACR_PASSWORD" \
  --dns-name-label "$DNS_APP" \
  --ports 8080 \
  --cpu 1 \
  --memory 2.0 \
  --environment-variables \
      SPRING_DATASOURCE_URL="jdbc:mysql://${DB_HOST}:3306/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
      SPRING_DATASOURCE_USERNAME="$DB_USER" \
      SPRING_DATASOURCE_PASSWORD="$DB_PASS"

echo "Aguardando 15 segundos para a inicialização da aplicação..."
sleep 15

APP_URL=$(az container show --resource-group "$RESOURCE_GROUP" --name "$APP_ACI_NAME" --query ipAddress.fqdn -o tsv)

echo ""
echo "=========================================================="
echo "🎉 DEPLOY DA SOLUÇÃO PETTRACK CONCLUÍDO COM SUCESSO!"
echo "----------------------------------------------------------"
echo "🌐 URL Pública da API: http://${APP_URL}:8080/pet/todos"
echo "📑 Documentação Swagger: http://${APP_URL}:8080/swagger"
echo "🗄️  Host do Banco:      ${DB_HOST}:3306"
echo "📦 Resource Group:     $RESOURCE_GROUP"
echo "=========================================================="
