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

-- Desabilita verificação de chaves estrangeiras para recriação limpa
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS tb_adesao_medicamento;
DROP TABLE IF EXISTS tb_medicamento;
DROP TABLE IF EXISTS tb_evento_clinico;
DROP TABLE IF EXISTS tb_alerta;
DROP TABLE IF EXISTS tb_collar_leitura;
DROP TABLE IF EXISTS tb_bcs_historico;
DROP TABLE IF EXISTS tb_score_historico;
DROP TABLE IF EXISTS tb_protocolo_preventivo;
DROP TABLE IF EXISTS tb_notificacao;
DROP TABLE IF EXISTS tb_usuario;
DROP TABLE IF EXISTS tb_pet;
DROP TABLE IF EXISTS tb_tutor;
DROP TABLE IF EXISTS tb_clinica;

DROP TABLE IF EXISTS seq_tutor;
DROP TABLE IF EXISTS seq_pet;
DROP TABLE IF EXISTS seq_clinica;
DROP TABLE IF EXISTS seq_usuario;
DROP TABLE IF EXISTS seq_alerta;
DROP TABLE IF EXISTS seq_evento_clinico;
DROP TABLE IF EXISTS seq_medicamento;
DROP TABLE IF EXISTS seq_adesao;
DROP TABLE IF EXISTS seq_bcs_hist;
DROP TABLE IF EXISTS seq_collar;
DROP TABLE IF EXISTS seq_notificacao;
DROP TABLE IF EXISTS seq_protocolo;
DROP TABLE IF EXISTS seq_score_hist;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- TABELAS PRINCIPAIS DO SISTEMA
-- =============================================================================

-- 1. TB_CLINICA (Clínicas Veterinárias Credenciadas)
CREATE TABLE tb_clinica (
    id_clinica BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único da clínica',
    nm_clinica VARCHAR(200) NOT NULL COMMENT 'Razão social ou nome fantasia da clínica',
    nr_cnpj VARCHAR(18) NOT NULL UNIQUE COMMENT 'CNPJ da clínica veterinária',
    ds_email VARCHAR(200) NULL COMMENT 'Email de contato da clínica',
    nr_telefone VARCHAR(20) NULL COMMENT 'Telefone da clínica',
    ds_endereco VARCHAR(300) NULL COMMENT 'Endereço da clínica',
    dt_cadastro DATE NOT NULL DEFAULT (CURRENT_DATE) COMMENT 'Data de cadastro da clínica'
) ENGINE=InnoDB COMMENT='Tabela de clínicas veterinárias credenciadas no PetTrack';

-- 2. TB_TUTOR (Tutores Proprietários de Animais)
CREATE TABLE tb_tutor (
    id_tutor BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único do tutor',
    nm_tutor VARCHAR(150) NOT NULL COMMENT 'Nome completo do tutor',
    ds_email VARCHAR(200) NOT NULL UNIQUE COMMENT 'Email de login/contato do tutor',
    nr_telefone VARCHAR(20) NULL COMMENT 'Telefone de contato do tutor',
    ds_endereco VARCHAR(300) NULL COMMENT 'Endereço residencial do tutor',
    dt_cadastro DATE NOT NULL DEFAULT (CURRENT_DATE) COMMENT 'Data de cadastro do tutor'
) ENGINE=InnoDB COMMENT='Tabela principal de tutores cadastrados';

-- 3. TB_PET (Animais de Estimação - Relacionado com Tutor e Clínica)
CREATE TABLE tb_pet (
    id_pet BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único do pet',
    nm_pet VARCHAR(100) NOT NULL COMMENT 'Nome do animal de estimação',
    ds_especie VARCHAR(50) NOT NULL COMMENT 'Espécie do pet (Canino, Felino, etc.)',
    ds_raca VARCHAR(100) NULL COMMENT 'Raça do animal',
    ds_sexo ENUM('M', 'F') NULL COMMENT 'Sexo do animal (M ou F)',
    nr_idade_anos DOUBLE NULL COMMENT 'Idade em anos',
    nr_peso_kg DOUBLE NULL COMMENT 'Peso corporal em kg',
    dt_cadastro DATE NOT NULL DEFAULT (CURRENT_DATE) COMMENT 'Data de cadastro do pet',
    id_tutor BIGINT NOT NULL COMMENT 'Chave estrangeira do tutor proprietário',
    id_clinica BIGINT NOT NULL COMMENT 'Chave estrangeira da clínica veterinária',
    CONSTRAINT fk_pet_tutor FOREIGN KEY (id_tutor) REFERENCES tb_tutor(id_tutor) ON DELETE CASCADE,
    CONSTRAINT fk_pet_clinica FOREIGN KEY (id_clinica) REFERENCES tb_clinica(id_clinica) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Tabela de animais de estimação monitorados';

-- 4. TB_USUARIO (Usuários para autenticação Spring Security)
CREATE TABLE tb_usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único do usuário',
    nm_nome VARCHAR(150) NOT NULL COMMENT 'Nome do usuário',
    ds_email VARCHAR(200) NOT NULL UNIQUE COMMENT 'Email de autenticação',
    ds_senha VARCHAR(255) NOT NULL COMMENT 'Hash BCrypt da senha',
    ds_role VARCHAR(50) NOT NULL COMMENT 'Role do usuário (ROLE_ADMIN, ROLE_VET, ROLE_TUTOR)',
    st_ativo VARCHAR(1) NOT NULL DEFAULT 'S' COMMENT 'Status ativo (S/N)',
    id_tutor BIGINT NULL COMMENT 'Tutor associado',
    id_clinica BIGINT NULL COMMENT 'Clínica associada',
    CONSTRAINT fk_usuario_tutor FOREIGN KEY (id_tutor) REFERENCES tb_tutor(id_tutor) ON DELETE SET NULL,
    CONSTRAINT fk_usuario_clinica FOREIGN KEY (id_clinica) REFERENCES tb_clinica(id_clinica) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='Tabela de autenticação e perfis de acesso';

-- 5. TB_ALERTA (Alertas Preventivos de Saúde do Pet)
CREATE TABLE tb_alerta (
    id_alerta BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único do alerta',
    tp_alerta VARCHAR(50) NOT NULL COMMENT 'Tipo do alerta (ADESAO, BCS_CRITICO, FEBRE, PESO, SEDENTARISMO)',
    ds_descricao VARCHAR(1000) NULL COMMENT 'Descrição do alerta',
    nr_valor_ref DOUBLE NULL COMMENT 'Valor de referência detectado',
    dt_alerta DATE NOT NULL DEFAULT (CURRENT_DATE) COMMENT 'Data do alerta',
    st_resolvido VARCHAR(1) NOT NULL DEFAULT 'N' COMMENT 'Status de resolução (S/N)',
    id_pet BIGINT NOT NULL COMMENT 'Pet vinculado ao alerta',
    CONSTRAINT fk_alerta_pet FOREIGN KEY (id_pet) REFERENCES tb_pet(id_pet) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Tabela de alertas de saúde animal';

-- 6. TB_EVENTO_CLINICO (Consultas, Exames, Cirurgias)
CREATE TABLE tb_evento_clinico (
    id_evento BIGINT AUTO_INCREMENT PRIMARY KEY,
    tp_evento VARCHAR(50) NOT NULL,
    dt_evento DATE NOT NULL,
    ds_diagnostico VARCHAR(1000) NULL,
    ds_observacao VARCHAR(2000) NULL,
    id_pet BIGINT NOT NULL,
    id_clinica BIGINT NOT NULL,
    CONSTRAINT fk_evento_pet FOREIGN KEY (id_pet) REFERENCES tb_pet(id_pet) ON DELETE CASCADE,
    CONSTRAINT fk_evento_clinica FOREIGN KEY (id_clinica) REFERENCES tb_clinica(id_clinica) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. TB_MEDICAMENTO (Prescrições Clínicas)
CREATE TABLE tb_medicamento (
    id_medicamento BIGINT AUTO_INCREMENT PRIMARY KEY,
    nm_medicamento VARCHAR(200) NOT NULL,
    ds_dosagem VARCHAR(100) NOT NULL,
    ds_frequencia VARCHAR(100) NOT NULL,
    dt_inicio DATE NOT NULL,
    dt_fim DATE NULL,
    id_evento BIGINT NOT NULL,
    CONSTRAINT fk_med_evento FOREIGN KEY (id_evento) REFERENCES tb_evento_clinico(id_evento) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. TB_ADESAO_MEDICAMENTO (Registro de Tomadas de Medicamentos)
CREATE TABLE tb_adesao_medicamento (
    id_adesao BIGINT AUTO_INCREMENT PRIMARY KEY,
    dt_dose DATE NOT NULL,
    st_tomou VARCHAR(1) NOT NULL,
    ds_observacao VARCHAR(500) NULL,
    id_medicamento BIGINT NOT NULL,
    CONSTRAINT fk_adesao_med FOREIGN KEY (id_medicamento) REFERENCES tb_medicamento(id_medicamento) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9. TB_BCS_HISTORICO (Body Condition Score)
CREATE TABLE tb_bcs_historico (
    id_bcs BIGINT AUTO_INCREMENT PRIMARY KEY,
    nr_bcs INT NULL,
    ds_foto_url VARCHAR(500) NULL,
    ds_observacao VARCHAR(1000) NULL,
    dt_analise DATE NOT NULL DEFAULT (CURRENT_DATE),
    id_pet BIGINT NOT NULL,
    CONSTRAINT fk_bcs_pet FOREIGN KEY (id_pet) REFERENCES tb_pet(id_pet) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 10. TB_COLLAR_LEITURA (Telemetria IoT da Coleira Clyvo)
CREATE TABLE tb_collar_leitura (
    id_leitura BIGINT AUTO_INCREMENT PRIMARY KEY,
    nr_temperatura DOUBLE NOT NULL,
    nr_atividade DOUBLE NULL,
    dt_leitura DATE NOT NULL DEFAULT (CURRENT_DATE),
    ds_topico_mqtt VARCHAR(200) NULL,
    id_pet BIGINT NOT NULL,
    CONSTRAINT fk_collar_pet FOREIGN KEY (id_pet) REFERENCES tb_pet(id_pet) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 11. TB_NOTIFICACAO (Notificações Enviadas ao Tutor)
CREATE TABLE tb_notificacao (
    id_notificacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    tp_notificacao VARCHAR(50) NOT NULL,
    ds_titulo VARCHAR(200) NOT NULL,
    ds_mensagem VARCHAR(2000) NULL,
    dt_envio DATE NOT NULL DEFAULT (CURRENT_DATE),
    st_lida VARCHAR(1) NOT NULL DEFAULT 'N',
    id_tutor BIGINT NOT NULL,
    id_pet BIGINT NOT NULL,
    CONSTRAINT fk_notif_tutor FOREIGN KEY (id_tutor) REFERENCES tb_tutor(id_tutor) ON DELETE CASCADE,
    CONSTRAINT fk_notif_pet FOREIGN KEY (id_pet) REFERENCES tb_pet(id_pet) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 12. TB_PROTOCOLO_PREVENTIVO (Vacinas, Vermífugos e Checkups)
CREATE TABLE tb_protocolo_preventivo (
    id_protocolo BIGINT AUTO_INCREMENT PRIMARY KEY,
    tp_protocolo VARCHAR(50) NOT NULL,
    nm_protocolo VARCHAR(200) NOT NULL,
    dt_aplicacao DATE NULL,
    dt_proxima DATE NULL,
    st_status VARCHAR(20) NOT NULL,
    id_pet BIGINT NOT NULL,
    CONSTRAINT fk_prot_pet FOREIGN KEY (id_pet) REFERENCES tb_pet(id_pet) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 13. TB_SCORE_HISTORICO (Histórico do Health Score Geral)
CREATE TABLE tb_score_historico (
    id_score BIGINT AUTO_INCREMENT PRIMARY KEY,
    nr_score DOUBLE NOT NULL,
    dt_registro DATE NOT NULL DEFAULT (CURRENT_DATE),
    ds_observacao VARCHAR(500) NULL,
    id_pet BIGINT NOT NULL,
    CONSTRAINT fk_score_pet FOREIGN KEY (id_pet) REFERENCES tb_pet(id_pet) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- TABELAS DE SEQUÊNCIA (Compatibilidade Hibernate SequenceGenerator)
-- =============================================================================
CREATE TABLE seq_tutor (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_tutor VALUES (100);
CREATE TABLE seq_pet (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_pet VALUES (100);
CREATE TABLE seq_clinica (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_clinica VALUES (100);
CREATE TABLE seq_usuario (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_usuario VALUES (100);
CREATE TABLE seq_alerta (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_alerta VALUES (100);
CREATE TABLE seq_evento_clinico (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_evento_clinico VALUES (100);
CREATE TABLE seq_medicamento (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_medicamento VALUES (100);
CREATE TABLE seq_adesao (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_adesao VALUES (100);
CREATE TABLE seq_bcs_hist (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_bcs_hist VALUES (100);
CREATE TABLE seq_collar (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_collar VALUES (100);
CREATE TABLE seq_notificacao (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_notificacao VALUES (100);
CREATE TABLE seq_protocolo (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_protocolo VALUES (100);
CREATE TABLE seq_score_hist (next_val BIGINT) ENGINE=InnoDB; INSERT INTO seq_score_hist VALUES (100);

-- =============================================================================
-- CARGA INICIAL COM DADOS SIGNIFICATIVOS (Pelo menos 2 linhas por tabela)
-- =============================================================================

INSERT INTO tb_clinica (id_clinica, nm_clinica, nr_cnpj, ds_email, nr_telefone, ds_endereco) VALUES
(1, 'Clyvo Vet Care - Jardins', '12.345.678/0001-90', 'contato@clyvovet.com.br', '1133334444', 'Alameda Santos, 800 - Jardins, SP'),
(2, 'Hospital Veterinário Animal Life', '98.765.432/0001-10', 'urgencias@animallife.com.br', '1135556666', 'Rua Domingos de Morais, 1200 - Vila Mariana, SP');

INSERT INTO tb_tutor (id_tutor, nm_tutor, ds_email, nr_telefone, ds_endereco) VALUES
(1, 'Carlos Eduardo Lima', 'carlos.lima@email.com', '11987654321', 'Av. Paulista, 1000 - Bela Vista, SP'),
(2, 'Beatriz Mendes Souza', 'beatriz.mendes@email.com', '11912345678', 'Rua Augusta, 500 - Consolação, SP'),
(3, 'Rodrigo Alves Silva', 'rodrigo.alves@email.com', '11977778888', 'Av. Faria Lima, 2500 - Itaim Bibi, SP');

INSERT INTO tb_pet (id_pet, nm_pet, ds_especie, ds_raca, ds_sexo, nr_idade_anos, nr_peso_kg, id_tutor, id_clinica) VALUES
(1, 'Thor', 'Canino', 'Golden Retriever', 'M', 3.5, 32.50, 1, 1),
(2, 'Luna', 'Felino', 'Siamês', 'F', 2.0, 4.20, 1, 1),
(3, 'Bob', 'Canino', 'Bulldog Francês', 'M', 1.8, 12.80, 2, 2),
(4, 'Mel', 'Canino', 'Poodle', 'F', 4.0, 6.10, 3, 2);

INSERT INTO tb_alerta (id_alerta, tp_alerta, ds_descricao, nr_valor_ref, st_resolvido, id_pet) VALUES
(1, 'FEBRE', 'Temperatura corporal detectada em 39.8°C acima do normal', 39.8, 'N', 1),
(2, 'SEDENTARISMO', 'Pet apresentou nível de atividade física 80% abaixo da média', 15.0, 'N', 3);
