-- 1. Insertar Roles
INSERT INTO role_table (name, status, created_at, disable_at)
VALUES ('ADMIN', 'ACTIVE', CURRENT_TIMESTAMP, NULL)
    ON CONFLICT DO NOTHING;

INSERT INTO role_table (name, status, created_at, disable_at)
VALUES ('USER', 'ACTIVE', CURRENT_TIMESTAMP, NULL)
    ON CONFLICT DO NOTHING;

-- 2. Insertar Usuario
INSERT INTO auth_table (id, username, email, password, status, created_at, disable_at)
VALUES (
           '550e8400-e29b-41d4-a716-446655440004',
           'admin',
           'admin123@example.com',
           '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00dmRe.qS8TQ4m',
           'ACTIVE',
           CURRENT_TIMESTAMP,
           NULL
       )
    ON CONFLICT DO NOTHING;

-- 3. Insertar Relación buscando el ID dinámicamente
INSERT INTO roles_auth (id_auth, id_role)
SELECT '550e8400-e29b-41d4-a716-446655440004', id
FROM role_table
WHERE name = 'ADMIN'
    ON CONFLICT DO NOTHING;