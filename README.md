# 🐾 PetTrack — API de Monitoramento de Saúde Animal

> Plataforma inteligente para rastreamento e gestão da saúde de pets, conectando tutores, clínicas e dispositivos IoT.

---

## Integrantes

Nome | RM
--- | ---
Gabriel Sbrana Campos | 565849
Moisés Waidemann Molinillo Júnior | 563719
Richard Freitas | 566127
Thiago Rodrigues da Mota | 563650

---

## 📋 Índice

1. [Descrição do Projeto](#-descrição-do-projeto)
2. [Benefícios para o Negócio](#-benefícios-para-o-negócio)
3. [Arquitetura Macro](#-arquitetura-macro)
4. [Tecnologias](#-tecnologias)
5. [Rotas da API](#-rotas-da-api)
6. [Instalação da Solução (How To)](#-instalação-da-solução-how-to)
7. [Dockerfile](#-dockerfile)
8. [Script Azure CLI](#-script-azure-cli)

---

## 📌 Descrição do Projeto

O **PetTrack** é uma API RESTful desenvolvida em **Java com Spring Boot** para o monitoramento contínuo da saúde de animais de estimação. A plataforma centraliza informações clínicas, eventos de saúde, alertas, medicamentos e leituras de dispositivos IoT (collar), permitindo que tutores e clínicas acompanhem o bem-estar dos pets em tempo real.

A solução é executada em containers Docker na nuvem Azure, garantindo escalabilidade, segurança e disponibilidade.

---

## 💼 Benefícios para o Negócio

- **Redução de riscos de saúde animal** com monitoramento contínuo via dispositivo collar IoT
- **Agilidade no atendimento clínico** com histórico centralizado de eventos e medicamentos
- **Engajamento do tutor** com notificações e alertas personalizados
- **Rastreabilidade completa** do histórico de saúde do pet desde o cadastro
- **Escalabilidade em nuvem** com infraestrutura Docker + Azure, reduzindo custos operacionais
- **Tomada de decisão baseada em dados** com score de saúde e histórico de BCS (Body Condition Score)

---

## 🏗️ Arquitetura Macro

```
┌─────────────┐        ┌──────────────────────────────────────────┐
│   Usuário   │──────▶ │              Azure VM (Ubuntu 22.04)      │
│  (Postman / │  8080  │                                           │
│  Navegador) │        │  ┌─────────────────┐  ┌───────────────┐  │
└─────────────┘        │  │  Container App  │  │ Container DB  │  │
                       │  │  Java/Spring    │──│ Oracle XE     │  │
                       │  │  porta: 8080    │  │ porta: 1521   │  │
                       │  └─────────────────┘  └───────────────┘  │
                       │         │                     │           │
                       │         └──── oracle-data ────┘           │
                       │              (volume nomeado)             │
                       └──────────────────────────────────────────┘
```

> Diagrama completo disponível no PDF de entrega e arquivo Draw.io

---

## 🛠️ Tecnologias

- **Java 21** + **Spring Boot**
- **Oracle XE 21** (containerizado — `gvenzl/oracle-xe:21-slim`)
- **Docker** (sem Docker Compose — containers individuais)
- **Maven** (build)
- **Swagger/OpenAPI** (`/swagger`)
- **Azure VM** (Ubuntu 22.04 — Standard D2s v3)
- **Azure CLI**

---

## 🔀 Rotas da API

Base URL: `http://<IP_DA_VM>:8080`
Documentação interativa: `http://<IP_DA_VM>:8080/swagger`

---

### 🐶 Pet — `/pet`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/pet/todos` | Lista todos os pets |
| GET | `/pet/paginar` | Lista pets com paginação |
| GET | `/pet/{id}` | Busca pet por ID |
| GET | `/pet/clinica/{idClinica}` | Lista pets por clínica |
| GET | `/pet/sexo` | Filtra pets por sexo |
| GET | `/pet/buscar` | Busca pets por parâmetros |
| GET | `/pet/alertasPendentes` | Lista pets com alertas pendentes |
| POST | `/pet/novo` | Cadastra novo pet |
| PUT | `/pet/atualizar/{id}` | Atualiza dados do pet |
| DELETE | `/pet/remover/{id}` | Remove pet |

---

### 👤 Tutor — `/tutor`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/tutor/todos` | Lista todos os tutores |
| GET | `/tutor/paginar` | Lista tutores com paginação |
| GET | `/tutor/{id}` | Busca tutor por ID |
| GET | `/tutor/nomePet` | Busca tutor pelo nome do pet |
| GET | `/tutor/nomeOuEmail` | Busca tutor por nome ou e-mail |
| POST | `/tutor/novo` | Cadastra novo tutor |
| PUT | `/tutor/atualizar/{id}` | Atualiza dados do tutor |
| DELETE | `/tutor/remover/{id}` | Remove tutor |

---

### 🏥 Clínica — `/clinica`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/clinica/todos` | Lista todas as clínicas |
| GET | `/clinica/paginar` | Lista clínicas com paginação |
| GET | `/clinica/{id}` | Busca clínica por ID |
| GET | `/clinica/buscar` | Busca clínica por parâmetros |
| GET | `/clinica/nomePet` | Busca clínica pelo nome do pet |
| POST | `/clinica/novo` | Cadastra nova clínica |
| PUT | `/clinica/atualizar/{id}` | Atualiza dados da clínica |
| DELETE | `/clinica/remover/{id}` | Remove clínica |

---

### 💊 Medicamento — `/medicamento`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/medicamento/todos` | Lista todos os medicamentos |
| GET | `/medicamento/paginar` | Lista com paginação |
| GET | `/medicamento/{id}` | Busca medicamento por ID |
| GET | `/medicamento/buscar` | Busca por parâmetros |
| GET | `/medicamento/ativos/{idPet}` | Lista medicamentos ativos do pet |
| POST | `/medicamento/novo` | Cadastra novo medicamento |
| PUT | `/medicamento/atualizar/{id}` | Atualiza medicamento |
| DELETE | `/medicamento/remover/{id}` | Remove medicamento |

---

### 💉 Adesão a Medicamento — `/adesao`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/adesao/todos` | Lista todas as adesões |
| GET | `/adesao/paginar` | Lista com paginação |
| GET | `/adesao/{id}` | Busca adesão por ID |
| GET | `/adesao/status` | Filtra por status |
| POST | `/adesao/novo` | Cadastra nova adesão |
| PUT | `/adesao/atualizar/{id}` | Atualiza adesão |
| DELETE | `/adesao/remover/{id}` | Remove adesão |

---

### 🏥 Evento Clínico — `/evento`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/evento/todos` | Lista todos os eventos |
| GET | `/evento/paginar` | Lista com paginação |
| GET | `/evento/{id}` | Busca evento por ID |
| GET | `/evento/tipo` | Filtra por tipo |
| GET | `/evento/medicamentos/{idPet}` | Lista eventos com medicamentos do pet |
| POST | `/evento/novo` | Cadastra novo evento |
| PUT | `/evento/atualizar/{id}` | Atualiza evento |
| DELETE | `/evento/remover/{id}` | Remove evento |

---

### 🔔 Notificação — `/notificacao`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/notificacao/todos` | Lista todas as notificações |
| GET | `/notificacao/paginar` | Lista com paginação |
| GET | `/notificacao/{id}` | Busca notificação por ID |
| GET | `/notificacao/status` | Filtra por status |
| GET | `/notificacao/tipo` | Filtra por tipo |
| GET | `/notificacao/urgentes/{idTutor}` | Lista notificações urgentes do tutor |
| POST | `/notificacao/novo` | Cadastra nova notificação |
| PUT | `/notificacao/atualizar/{id}` | Atualiza notificação |
| DELETE | `/notificacao/remover/{id}` | Remove notificação |

---

### ⚠️ Alerta — `/alerta`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/alerta/todos` | Lista todos os alertas |
| GET | `/alerta/paginar` | Lista com paginação |
| GET | `/alerta/{id}` | Busca alerta por ID |
| GET | `/alerta/tipo` | Filtra por tipo |
| GET | `/alerta/pendentes/{idPet}` | Lista alertas pendentes do pet |
| POST | `/alerta/novo` | Cadastra novo alerta |
| PUT | `/alerta/atualizar/{id}` | Atualiza alerta |
| DELETE | `/alerta/remover/{id}` | Remove alerta |

---

### 🛰️ Collar Leitura (IoT) — `/collar`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/collar/todos` | Lista todas as leituras |
| GET | `/collar/paginar` | Lista com paginação |
| GET | `/collar/{id}` | Busca leitura por ID |
| GET | `/collar/temperatura` | Filtra por temperatura |
| GET | `/collar/ultima/{idPet}` | Última leitura do pet |
| POST | `/collar/novo` | Registra nova leitura |
| PUT | `/collar/atualizar/{id}` | Atualiza leitura |
| DELETE | `/collar/remover/{id}` | Remove leitura |

---

### 📊 BCS Histórico — `/bcs`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/bcs/todos` | Lista todo o histórico |
| GET | `/bcs/paginar` | Lista com paginação |
| GET | `/bcs/{id}` | Busca registro por ID |
| GET | `/bcs/historico/{idPet}` | Histórico de BCS do pet |
| GET | `/bcs/media/{idPet}` | Média de BCS do pet |
| POST | `/bcs/novo` | Cadastra novo registro |
| PUT | `/bcs/atualizar/{id}` | Atualiza registro |
| DELETE | `/bcs/remover/{id}` | Remove registro |

---

### 🏆 Score Histórico — `/score`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/score/todos` | Lista todo o histórico |
| GET | `/score/paginar` | Lista com paginação |
| GET | `/score/{id}` | Busca score por ID |
| GET | `/score/historico/{idPet}` | Histórico de score do pet |
| GET | `/score/media/{idPet}` | Média de score do pet |
| POST | `/score/novo` | Cadastra novo score |
| PUT | `/score/atualizar/{id}` | Atualiza score |
| DELETE | `/score/remover/{id}` | Remove score |

---

### 🛡️ Protocolo Preventivo — `/protocolo`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/protocolo/todos` | Lista todos os protocolos |
| GET | `/protocolo/paginar` | Lista com paginação |
| GET | `/protocolo/{id}` | Busca protocolo por ID |
| GET | `/protocolo/tipo` | Filtra por tipo |
| GET | `/protocolo/pendentes/{idPet}` | Lista protocolos pendentes do pet |
| POST | `/protocolo/novo` | Cadastra novo protocolo |
| PUT | `/protocolo/atualizar/{id}` | Atualiza protocolo |
| DELETE | `/protocolo/remover/{id}` | Remove protocolo |

---

## 🚀 Instalação da Solução (How To)

### Pré-requisitos

- Docker instalado na máquina / VM
- Git instalado
- Acesso à internet

---

### Passo 1 — Clonar o repositório

```bash
git clone https://github.com/Challenge-PetTrack/JAVA-ADVANCED.git
cd pettrack
```

---

### Passo 2 — Criar a rede Docker

```bash
docker network create petnet
```

---

### Passo 3 — Subir o banco Oracle

```bash
# Clonar o repositório do banco
git clone https://github.com/Challenge-PetTrack/MASTERING-RELATIONAL-NON-RELATIONAL-DATABASE.git
cd pettrack-modeler

# Build e execução do container Oracle
docker build -t pettrack-oracle .

docker run -d \
  --name pettrack-oracle \
  --network petnet \
  -p 1521:1521 \
  -e ORACLE_PASSWORD=111206 \
  -e APP_USER=rm563719 \
  -e APP_USER_PASSWORD=111206 \
  -v oracle-data:/opt/oracle/oradata \
  pettrack-oracle
```

> ⏳ Aguarde ~90 segundos para o Oracle inicializar completamente.

---

### Passo 4 — Verificar se o Oracle está pronto

```bash
docker logs pettrack-oracle | tail -20
# Procure pela mensagem: "DATABASE IS READY TO USE!"
```

---

### Passo 5 — Subir a aplicação Java

```bash
cd ../pettrack

# Build da imagem
docker build -t pettrack-app .

# Executar o container
docker run -d \
  --name pettrack-app \
  --network petnet \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL \
  -e SPRING_DATASOURCE_USERNAME=rm563719 \
  -e SPRING_DATASOURCE_PASSWORD=111206 \
  pettrack-app
```

---

### Passo 6 — Verificar se está rodando

```bash
docker ps
# Ambos os containers devem aparecer como "Up"

# Testar a aplicação
curl http://localhost:8080/tutor/todos
```

---

### Passo 7 — Acessar o Swagger

```
http://localhost:8080/swagger
```

---

### Comandos úteis

```bash
# Ver logs da aplicação
docker logs pettrack-app

# Parar os containers
docker stop pettrack-app pettrack-oracle

# Iniciar novamente (dados persistidos no volume)
docker start pettrack-oracle
docker start pettrack-app

# Remover tudo (exceto o volume)
docker rm -f pettrack-app pettrack-oracle
```

---

## 🐳 Dockerfile

### Aplicação Java (`pettrack/Dockerfile`)

```dockerfile
FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

COPY . /app

ENV SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
ENV SPRING_DATASOURCE_USERNAME=rm563719
ENV SPRING_DATASOURCE_PASSWORD=111206

RUN adduser -h /home/appuser -s /bin/bash -D appuser

USER appuser

EXPOSE 8080

CMD ["bash", "-c", "mvn clean package -DskipTests && java -jar target/*.jar"]
```

### Banco Oracle (`pettrack-modeler/Dockerfile`)

```dockerfile
FROM gvenzl/oracle-xe:21-slim

ENV ORACLE_PASSWORD=$111206
ENV APP_USER=$rm563719
ENV APP_USER_PASSWORD=$111206

RUN mkdir -p /container-entrypoint-initdb.d
COPY pettrack.sql /container-entrypoint-initdb.d/pettrack.sql

EXPOSE 1521
```

---

## ☁️ Script Azure CLI

```# Variáveis principais
# chmod +x challenge-scripts.sh
# sed -i 's/\r$//' challenge-scripts.sh
# ./challenge-scripts.sh
GRUPO=pettrack
LOCATION=eastus2
USER=azureuser
PASSWORD='Moises12@'

RG=rg-$GRUPO
VNET=vnet-$GRUPO
SUBNET=subnet-$GRUPO
NSG=nsg-$GRUPO
VM=vm-$GRUPO

# 1. Resource Group
az group create \
  --name $RG \
  --location $LOCATION \
  --tags owner=$GRUPO environment=dev cost-center=fiap

# 2. VNet e Subnet
az network vnet create \
  --resource-group $RG \
  --name $VNET \
  --address-prefix 10.10.0.0/16 \
  --subnet-name $SUBNET \
  --subnet-prefix 10.10.1.0/24 \
  --tags owner=$GRUPO environment=dev cost-center=fiap

# 3. NSG
az network nsg create \
  --resource-group $RG \
  --name $NSG \
  --tags owner=$GRUPO environment=dev cost-center=fiap

# 4. Regras do NSG
az network nsg rule create \
  --resource-group $RG \
  --nsg-name $NSG \
  --name allow-ssh \
  --protocol Tcp \
  --priority 1000 \
  --destination-port-range 22 \
  --access Allow

az network nsg rule create \
  --resource-group $RG \
  --nsg-name $NSG \
  --name allow-http \
  --protocol Tcp \
  --priority 1001 \
  --destination-port-range 80 \
  --access Allow

az network nsg rule create \
  --resource-group $RG \
  --nsg-name $NSG \
  --name allow-8080 \
  --protocol Tcp \
  --priority 1002 \
  --destination-port-range 8080 \
  --access Allow

# 5. Associar NSG à subnet
az network vnet subnet update \
  --resource-group $RG \
  --vnet-name $VNET \
  --name $SUBNET \
  --network-security-group $NSG

# 6. Criar VM Ubuntu com autenticação por password
az vm create \
  --resource-group $RG \
  --name $VM \
  --image Ubuntu2204 \
  --admin-username $USER \
  --admin-password $PASSWORD \
  --authentication-type password \
  --size Standard_D2s_v3 \
  --vnet-name $VNET \
  --subnet $SUBNET \
  --nsg $NSG \
  --tags owner=$GRUPO environment=dev cost-center=fiap

# 7. Instalar Docker, Git e Nano remotamente, sem SSH
az vm run-command invoke \
  --resource-group $RG \
  --name $VM \
  --command-id RunShellScript \
  --scripts '
    export DEBIAN_FRONTEND=noninteractive
    sudo apt-get update -y
    sudo apt-get install -y ca-certificates curl git nano

    sudo install -m 0755 -d /etc/apt/keyrings
    sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
    sudo chmod a+r /etc/apt/keyrings/docker.asc

    sudo tee /etc/apt/sources.list.d/docker.sources > /dev/null <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
EOF

    sudo apt-get update -y
    sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

    sudo systemctl enable docker
    sudo systemctl start docker
    sudo usermod -aG docker azureuser
  '
```

---

## 📎 Links

- **Vídeo YouTube:** *https://youtu.be/WMwEoHx7LzA **
- **Repositório App:** *https://github.com/Challenge-PetTrack/JAVA-ADVANCED.git **
- **Repositório DB:** *https://github.com/Challenge-PetTrack/MASTERING-RELATIONAL-NON-RELATIONAL-DATABASE.git **
- **Repositório DevOps:** *https://github.com/Challenge-PetTrack/DEVOPS-TOOLS-CLOUD-COMPUTING.git **
