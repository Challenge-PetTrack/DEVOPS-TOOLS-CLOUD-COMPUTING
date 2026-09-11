-- =============================================================================
-- V3: CARGA DE DADOS INICIAIS E USUÁRIOS DO SISTEMA
-- Garante estrutura correta de TB_USUARIO e inserções idempotentes
-- =============================================================================

-- Garante que TB_USUARIO possui a coluna DS_ROLE e estrutura correta
DECLARE
    v_has_col NUMBER;
    v_has_tab NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_has_col FROM user_tab_cols WHERE table_name = 'TB_USUARIO' AND column_name = 'DS_ROLE';
    IF v_has_col = 0 THEN
        SELECT COUNT(*) INTO v_has_tab FROM user_tables WHERE table_name = 'TB_USUARIO';
        IF v_has_tab > 0 THEN
            EXECUTE IMMEDIATE 'DROP TABLE TB_USUARIO CASCADE CONSTRAINTS';
        END IF;

        EXECUTE IMMEDIATE 'CREATE TABLE TB_USUARIO (
            ID_USUARIO NUMBER        NOT NULL,
            NM_NOME    VARCHAR2(150) NOT NULL,
            DS_EMAIL   VARCHAR2(200) NOT NULL,
            DS_SENHA   VARCHAR2(255) NOT NULL,
            DS_ROLE    VARCHAR2(50)  NOT NULL,
            ST_ATIVO   CHAR(1)       DEFAULT ''S'' NOT NULL,
            ID_TUTOR   NUMBER,
            ID_CLINICA NUMBER,
            CONSTRAINT TB_USUARIO_PK       PRIMARY KEY (ID_USUARIO),
            CONSTRAINT TB_USUARIO_EMAIL_UN UNIQUE (DS_EMAIL),
            CONSTRAINT CK_USUARIO_ROLE     CHECK (DS_ROLE IN (''ROLE_ADMIN'', ''ROLE_VET'', ''ROLE_TUTOR'')),
            CONSTRAINT CK_USUARIO_ATIVO    CHECK (ST_ATIVO IN (''N'', ''S'')),
            CONSTRAINT FK_USUARIO_TUTOR    FOREIGN KEY (ID_TUTOR) REFERENCES TB_TUTOR (ID_TUTOR),
            CONSTRAINT FK_USUARIO_CLINICA  FOREIGN KEY (ID_CLINICA) REFERENCES TB_CLINICA (ID_CLINICA)
        )';
    END IF;
END;
/

-- 1. Inserção de Usuários para Spring Security
INSERT INTO TB_USUARIO (ID_USUARIO, NM_NOME, DS_EMAIL, DS_SENHA, DS_ROLE, ST_ATIVO)
SELECT SEQ_USUARIO.NEXTVAL, 'Administrador PetTrack', 'admin@pettrack.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ROLE_ADMIN', 'S'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM TB_USUARIO WHERE DS_EMAIL = 'admin@pettrack.com');

INSERT INTO TB_USUARIO (ID_USUARIO, NM_NOME, DS_EMAIL, DS_SENHA, DS_ROLE, ST_ATIVO, ID_CLINICA)
SELECT SEQ_USUARIO.NEXTVAL, 'Dra. Camila Veterinária', 'vet@pettrack.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ROLE_VET', 'S', 1
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM TB_USUARIO WHERE DS_EMAIL = 'vet@pettrack.com');

INSERT INTO TB_USUARIO (ID_USUARIO, NM_NOME, DS_EMAIL, DS_SENHA, DS_ROLE, ST_ATIVO, ID_TUTOR)
SELECT SEQ_USUARIO.NEXTVAL, 'Ana Paula Silva (Tutor)', 'ana.silva@icloud.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ROLE_TUTOR', 'S', 1
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM TB_USUARIO WHERE DS_EMAIL = 'ana.silva@icloud.com');

INSERT INTO TB_USUARIO (ID_USUARIO, NM_NOME, DS_EMAIL, DS_SENHA, DS_ROLE, ST_ATIVO, ID_TUTOR)
SELECT SEQ_USUARIO.NEXTVAL, 'Carlos Eduardo (Tutor)', 'carlos.santos@icloud.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ROLE_TUTOR', 'S', 2
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM TB_USUARIO WHERE DS_EMAIL = 'carlos.santos@icloud.com');

-- 2. Clínicas Veterinárias (insere somente se ainda não existirem)
INSERT INTO TB_CLINICA (ID_CLINICA, NM_CLINICA, NR_CNPJ, DS_EMAIL, NR_TELEFONE, DS_ENDERECO, DT_CADASTRO)
SELECT SEQ_CLINICA.NEXTVAL, 'VetLife Clínica Veterinária', '758748948123958473', 'vetlife@gmail.com', '11986758764', 'Rua Gamelinha, 99', SYSDATE
FROM dual WHERE NOT EXISTS (SELECT 1 FROM TB_CLINICA WHERE NR_CNPJ = '758748948123958473');

INSERT INTO TB_CLINICA (ID_CLINICA, NM_CLINICA, NR_CNPJ, DS_EMAIL, NR_TELEFONE, DS_ENDERECO, DT_CADASTRO)
SELECT SEQ_CLINICA.NEXTVAL, 'PetCare Hospital Animal', '455874764815675783', 'petcare@gmail.com', '11945758765', 'Rua Alfredo, 143', SYSDATE
FROM dual WHERE NOT EXISTS (SELECT 1 FROM TB_CLINICA WHERE NR_CNPJ = '455874764815675783');

-- 3. Tutores (insere somente se ainda não existirem)
INSERT INTO TB_TUTOR (ID_TUTOR, NM_TUTOR, DS_EMAIL, NR_TELEFONE, DS_ENDERECO, DT_CADASTRO)
SELECT SEQ_TUTOR.NEXTVAL, 'Ana Paula Silva', 'ana.silva@icloud.com', '11975647387', 'Rua José, 89', SYSDATE
FROM dual WHERE NOT EXISTS (SELECT 1 FROM TB_TUTOR WHERE DS_EMAIL = 'ana.silva@icloud.com');

INSERT INTO TB_TUTOR (ID_TUTOR, NM_TUTOR, DS_EMAIL, NR_TELEFONE, DS_ENDERECO, DT_CADASTRO)
SELECT SEQ_TUTOR.NEXTVAL, 'Carlos Eduardo Santos', 'carlos.santos@icloud.com', '11938768576', 'Rua Pedro de Souza, 56', SYSDATE
FROM dual WHERE NOT EXISTS (SELECT 1 FROM TB_TUTOR WHERE DS_EMAIL = 'carlos.santos@icloud.com');
