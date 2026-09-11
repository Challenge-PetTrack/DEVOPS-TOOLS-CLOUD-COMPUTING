-- =============================================================================
-- PROJETO: PETTRACK - SISTEMA DE MONITORAMENTO E SAÚDE ANIMAL (CLYVO VET)
-- DISCIPLINA: DevOps Tools & Cloud Computing - Sprint 3
-- INSTITUIÇÃO: FIAP - Challenge 2026 (2º Ano ADS)
-- INTEGRANTES:
--   - Gabriel Sbrana Campos     - RM 565849
--   - Moisés Waidemann          - RM 563719
--   - Richard Freitas           - RM 566127
--   - Thiago Rodrigues da Mota  - RM 563765
-- =============================================================================

CREATE DATABASE IF NOT EXISTS pettrack CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pettrack;

-- =============================================================================
-- TABELAS CORE DA APLICAÇÃO (Relacionamento 1:N entre Tutor e Pet)
-- =============================================================================

-- Tabela 1: TB_TUTOR (Entidade Pai - Core do Sistema)
CREATE TABLE IF NOT EXISTS tb_tutor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único do tutor',
    nome VARCHAR(100) NOT NULL COMMENT 'Nome completo do tutor',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email principal do tutor',
    telefone VARCHAR(20) NULL COMMENT 'Telefone de contato do tutor',
    endereco VARCHAR(300) NULL COMMENT 'Endereço residencial do tutor',
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Data e hora do cadastro do tutor'
) ENGINE=InnoDB COMMENT='Tabela principal de tutores cadastrados na plataforma PetTrack';

-- Tabela 2: TB_CLINICA (Entidade de Clínicas Veterinárias Parceiras)
CREATE TABLE IF NOT EXISTS tb_clinica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único da clínica',
    nome VARCHAR(100) NOT NULL COMMENT 'Razão social ou nome fantasia da clínica',
    cnpj VARCHAR(20) NOT NULL UNIQUE COMMENT 'CNPJ da clínica veterinária',
    email VARCHAR(100) NOT NULL COMMENT 'Email de contato da clínica',
    telefone VARCHAR(20) NULL COMMENT 'Telefone da clínica',
    endereco VARCHAR(300) NULL COMMENT 'Endereço da clínica',
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de credenciamento'
) ENGINE=InnoDB COMMENT='Tabela de clínicas veterinárias credenciadas no PetTrack';

-- Tabela 3: TB_PET (Entidade Filha - Relacionamento 1:N com Tutor e Clínica)
CREATE TABLE IF NOT EXISTS tb_pet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único do pet',
    tutor_id BIGINT NOT NULL COMMENT 'Chave estrangeira do tutor proprietário',
    clinica_id BIGINT NULL COMMENT 'Chave estrangeira da clínica de referência',
    nome VARCHAR(100) NOT NULL COMMENT 'Nome do animal de estimação',
    especie VARCHAR(50) NOT NULL COMMENT 'Espécie do animal (Canino, Felino, etc.)',
    raca VARCHAR(50) NOT NULL COMMENT 'Raça do animal',
    data_nascimento DATE NOT NULL COMMENT 'Data de nascimento estimada do pet',
    peso_kg DECIMAL(5,2) NOT NULL COMMENT 'Peso corporal atual em quilogramas',
    sexo VARCHAR(10) NOT NULL COMMENT 'Sexo do animal (Macho ou Femea)',
    collar_code VARCHAR(50) NULL UNIQUE COMMENT 'Código identificador da coleira IoT Clyvo',
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de cadastro do animal',
    CONSTRAINT fk_pet_tutor FOREIGN KEY (tutor_id) REFERENCES tb_tutor(id) ON DELETE CASCADE,
    CONSTRAINT fk_pet_clinica FOREIGN KEY (clinica_id) REFERENCES tb_clinica(id) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='Tabela principal dos animais de estimação monitorados';

-- Tabela 4: TB_ALERTA (Alertas gerados pela telemetria do Pet)
CREATE TABLE IF NOT EXISTS tb_alerta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único do alerta',
    pet_id BIGINT NOT NULL COMMENT 'Chave estrangeira do pet que gerou o alerta',
    tipo_alerta VARCHAR(50) NOT NULL COMMENT 'Tipo do alerta (Temperatura, Batimentos, Sedentarismo)',
    descricao VARCHAR(300) NOT NULL COMMENT 'Descrição detalhada da anomalia detectada',
    status VARCHAR(20) DEFAULT 'PENDENTE' COMMENT 'Status do alerta (PENDENTE, TRATADO, IGNORADO)',
    data_geracao DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Data e hora da geração do alerta',
    CONSTRAINT fk_alerta_pet FOREIGN KEY (pet_id) REFERENCES tb_pet(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Tabela de alertas preventivos de saúde animal';

-- =============================================================================
-- CARGA INICIAL COM DADOS SIGNIFICATIVOS (Pelo menos 2 linhas por tabela)
-- =============================================================================

INSERT INTO tb_tutor (nome, email, telefone, endereco) VALUES
('Carlos Eduardo Lima', 'carlos.lima@email.com', '11987654321', 'Av. Paulista, 1000 - Bela Vista, SP'),
('Beatriz Mendes Souza', 'beatriz.mendes@email.com', '11912345678', 'Rua Augusta, 500 - Consolação, SP'),
('Rodrigo Alves Silva', 'rodrigo.alves@email.com', '11977778888', 'Av. Faria Lima, 2500 - Itaim Bibi, SP');

INSERT INTO tb_clinica (nome, cnpj, email, telefone, endereco) VALUES
('Clyvo Vet Care - Jardins', '12.345.678/0001-90', 'contato@clyvovet.com.br', '1133334444', 'Alameda Santos, 800 - Jardins, SP'),
('Hospital Veterinário Animal Life', '98.765.432/0001-10', 'urgencias@animallife.com.br', '1135556666', 'Rua Domingos de Morais, 1200 - Vila Mariana, SP');

INSERT INTO tb_pet (tutor_id, clinica_id, nome, especie, raca, data_nascimento, peso_kg, sexo, collar_code) VALUES
(1, 1, 'Thor', 'Canino', 'Golden Retriever', '2021-06-15', 32.50, 'Macho', 'CLYVO-COLLAR-001'),
(1, 1, 'Luna', 'Felino', 'Siamês', '2022-03-10', 4.20, 'Femea', 'CLYVO-COLLAR-002'),
(2, 2, 'Bob', 'Canino', 'Bulldog Francês', '2023-01-20', 12.80, 'Macho', 'CLYVO-COLLAR-003'),
(3, 2, 'Mel', 'Canino', 'Poodle', '2020-11-05', 6.10, 'Femea', 'CLYVO-COLLAR-004');

INSERT INTO tb_alerta (pet_id, tipo_alerta, descricao, status) VALUES
(1, 'TEMPERATURA_ELEVADA', 'Temperatura corporal detectada em 39.8°C acima do limiar normal', 'PENDENTE'),
(3, 'BAIXA_ATIVIDADE', 'Pet apresentou 85% de inatividade física nas últimas 24 horas', 'PENDENTE');
