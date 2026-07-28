-- =============================================================================
--  Prueba Técnica Full Stack - PeriferiaIT
--  Red social con Angular 22 + Spring Boot 3
--
--  Script de inicialización de base de datos PostgreSQL.
--  Crea schemas, tablas, procedimientos/funciones PL/pgSQL y datos seed.
--
--  Ejecutar directamente:  psql -U postgres -d periferia -f database/init.sql
--
--  Arquitectura de datos (2 microservicios, 1 Postgres):
--    - schema  auth    -> periferiait-auth-service   (usuarios / perfiles / login)
--    - schema  social  -> periferiait-logic-service  (publicaciones / likes)
--
--  El script es idempotente: puede re-ejecutarse sin error
--  (CREATE ... IF NOT EXISTS / OR REPLACE, seeds con ON CONFLICT DO NOTHING).
--
--  Usuarios seed  ->  password (en claro) para TODOS: Periferia123*
--  Hashes almacenados: BCrypt (cost 10), compatibles con Spring Security.
-- =============================================================================

-- Extensión para gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =============================================================================
--  SCHEMAS
-- =============================================================================
CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS social;

-- =============================================================================
--  SCHEMA auth  (periferiait-auth-service)
-- =============================================================================

CREATE TABLE IF NOT EXISTS auth.users (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    first_name    VARCHAR(80)  NOT NULL,       -- Nombres
    last_name     VARCHAR(80)  NOT NULL,       -- Apellidos
    birth_date    DATE         NOT NULL,       -- Fecha de nacimiento
    alias         VARCHAR(50)  NOT NULL UNIQUE, -- Alias
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_users_username ON auth.users (username);

-- =============================================================================
--  SCHEMA social  (periferiait-logic-service)
-- =============================================================================

CREATE TABLE IF NOT EXISTS social.posts (
    id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    author_id    UUID         NOT NULL,            -- FK lógica a auth.users.id
    author_alias VARCHAR(50)  NOT NULL,            -- denormalizado (desde el JWT)
    message      TEXT         NOT NULL,            -- Mensaje de la publicación
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now() -- Fecha (default al guardar)
);

CREATE INDEX IF NOT EXISTS idx_posts_created_at ON social.posts (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_posts_author_id  ON social.posts (author_id);

CREATE TABLE IF NOT EXISTS social.post_likes (
    id         UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    post_id    UUID        NOT NULL REFERENCES social.posts (id) ON DELETE CASCADE,
    user_id    UUID        NOT NULL,               -- quién dio like (auth.users.id)
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_post_user_like UNIQUE (post_id, user_id) -- un like por usuario/post
);

CREATE INDEX IF NOT EXISTS idx_post_likes_post_id ON social.post_likes (post_id);

-- Auditoría de likes (extra: logs / auditoría)
CREATE TABLE IF NOT EXISTS social.like_audit (
    id         BIGINT      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    post_id    UUID        NOT NULL,
    user_id    UUID        NOT NULL,
    action     VARCHAR(10) NOT NULL,               -- LIKE | UNLIKE
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =============================================================================
--  PROCEDIMIENTOS Y FUNCIONES PL/pgSQL
--  Requisito: mínimo 2 PROCEDURE (PLSQL). Se incluyen 2 PROCEDURE + 2 FUNCTION.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- PROCEDURE 1: social.sp_toggle_like
--   Da/quita like de forma atómica (solo parámetros IN) y registra la acción
--   en la tabla de auditoría. El total actualizado se obtiene aparte con la
--   función fn_post_like_count (se evita OUT/INOUT, no soportado por pgjdbc
--   al extraer parámetros de salida en un CALL de procedimiento).
--   Uso Spring/JPA:  CALL social.sp_toggle_like(:postId, :userId)
-- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE social.sp_toggle_like(
    IN p_post_id UUID,
    IN p_user_id UUID
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_exists BOOLEAN;
BEGIN
    SELECT EXISTS (
        SELECT 1 FROM social.post_likes
        WHERE post_id = p_post_id AND user_id = p_user_id
    ) INTO v_exists;

    IF v_exists THEN
        DELETE FROM social.post_likes
        WHERE post_id = p_post_id AND user_id = p_user_id;

        INSERT INTO social.like_audit (post_id, user_id, action)
        VALUES (p_post_id, p_user_id, 'UNLIKE');
    ELSE
        INSERT INTO social.post_likes (post_id, user_id)
        VALUES (p_post_id, p_user_id);

        INSERT INTO social.like_audit (post_id, user_id, action)
        VALUES (p_post_id, p_user_id, 'LIKE');
    END IF;
END;
$$;

-- -----------------------------------------------------------------------------
-- PROCEDURE 2: social.sp_create_post
--   Crea una publicación con el id provisto por la aplicación (IN) y fecha por
--   defecto (now()). Sin OUT/INOUT: pgjdbc no soporta extraer un parámetro de
--   salida UUID en un CALL de procedimiento.
--   Uso Spring/JPA:  CALL social.sp_create_post(:id, :authorId, :alias, :msg)
-- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE social.sp_create_post(
    IN p_id           UUID,
    IN p_author_id    UUID,
    IN p_author_alias VARCHAR,
    IN p_message      TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO social.posts (id, author_id, author_alias, message)
    VALUES (p_id, p_author_id, p_author_alias, p_message);
END;
$$;

-- -----------------------------------------------------------------------------
-- FUNCTION 1: social.fn_post_like_count
--   Total de likes de una publicación.
--   Uso Spring/JPA:  SELECT social.fn_post_like_count(:postId)
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION social.fn_post_like_count(p_post_id UUID)
RETURNS INTEGER
LANGUAGE plpgsql
STABLE
AS $$
DECLARE
    v_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM social.post_likes
    WHERE post_id = p_post_id;
    RETURN v_count;
END;
$$;

-- -----------------------------------------------------------------------------
-- FUNCTION 2: auth.fn_get_profile
--   Perfil de un usuario (sin exponer el hash de contraseña).
--   Uso Spring/JPA:  SELECT * FROM auth.fn_get_profile(:userId)
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION auth.fn_get_profile(p_user_id UUID)
RETURNS TABLE (
    id         UUID,
    username   VARCHAR,
    first_name VARCHAR,
    last_name  VARCHAR,
    birth_date DATE,
    alias      VARCHAR,
    created_at TIMESTAMPTZ
)
LANGUAGE plpgsql
STABLE
AS $$
BEGIN
    RETURN QUERY
    SELECT u.id, u.username, u.first_name, u.last_name,
           u.birth_date, u.alias, u.created_at
    FROM auth.users u
    WHERE u.id = p_user_id;
END;
$$;

-- =============================================================================
--  SEED DE DATOS
--  Usuarios de prueba (UUID fijos) + 1 publicación por usuario.
--  Password en claro para todos: Periferia123*
-- =============================================================================

INSERT INTO auth.users (id, username, password_hash, first_name, last_name, birth_date, alias) VALUES
    ('11111111-1111-1111-1111-111111111111', 'jdoe',      '$2b$10$H1fW6CUC7vnWMv9suX24leTgILRQWwAzgu6EDc/W6ViKF7cXo7mKi', 'Juan',   'Pérez',     '1990-05-14', 'juanp'),
    ('22222222-2222-2222-2222-222222222222', 'mgarcia',   '$2b$10$r32HxnBdjCAtJ4btp3mxEuMwlKWt8hbgUEJ/o3w5IBHBnuFM9JJSu', 'María',  'García',    '1988-11-02', 'mariag'),
    ('33333333-3333-3333-3333-333333333333', 'crodriguez','$2b$10$d0Pxse7UmRNR5O23jlFtleY9SOEosY/QO2XFQAa38oyqa.I47QySu', 'Carlos', 'Rodríguez', '1995-03-21', 'carlosr'),
    ('44444444-4444-4444-4444-444444444444', 'alopez',    '$2b$10$X9uIHgIEFpfIKLg0j.1qfOWi1j.c5.8kQCCMkOmVu5ifyE2RcAQFW', 'Ana',    'López',     '1992-07-09', 'anal'),
    ('55555555-5555-5555-5555-555555555555', 'dmartinez', '$2b$10$ld7PXiiZyS35AC5icKmRFOpSDcuVfq4D8iBiCon3UfW45EOE3SPhi', 'Diego',  'Martínez',  '1985-12-30', 'diegom')
ON CONFLICT (id) DO NOTHING;

-- Una publicación por usuario (alias denormalizado, UUID fijo para idempotencia)
INSERT INTO social.posts (id, author_id, author_alias, message) VALUES
    ('aaaa1111-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'juanp',   '¡Hola! Este es mi primer post en la red social de Periferia. 🚀'),
    ('aaaa1111-0000-0000-0000-000000000002', '22222222-2222-2222-2222-222222222222', 'mariag',  'Aprendiendo Angular 22 con Signals, me encanta lo reactivo. ✨'),
    ('aaaa1111-0000-0000-0000-000000000003', '33333333-3333-3333-3333-333333333333', 'carlosr', 'Spring Boot 3 + microservicios = combinación poderosa. 💪'),
    ('aaaa1111-0000-0000-0000-000000000004', '44444444-4444-4444-4444-444444444444', 'anal',    'Los likes en tiempo real con WebSocket funcionan increíble. ⚡'),
    ('aaaa1111-0000-0000-0000-000000000005', '55555555-5555-5555-5555-555555555555', 'diegom',  'PostgreSQL con procedures PL/pgSQL, todo bien optimizado. 🐘')
ON CONFLICT (id) DO NOTHING;

-- Algunos likes de ejemplo para que el contador no arranque en cero
INSERT INTO social.post_likes (post_id, user_id) VALUES
    ('aaaa1111-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222'),
    ('aaaa1111-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333'),
    ('aaaa1111-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111'),
    ('aaaa1111-0000-0000-0000-000000000003', '44444444-4444-4444-4444-444444444444')
ON CONFLICT (post_id, user_id) DO NOTHING;

-- =============================================================================
--  FIN DEL SCRIPT
-- =============================================================================
