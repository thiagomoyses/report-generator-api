-- ---------- Students ----------
INSERT INTO students (name, email, birth_date) VALUES
    ('Ana Souza',    'ana.souza@example.com',    '2005-03-14'),
    ('Bruno Lima',   'bruno.lima@example.com',   '2004-11-02'),
    ('Carla Mendes', 'carla.mendes@example.com', '2005-07-21'),
    ('Diego Rocha',  'diego.rocha@example.com',  '2005-01-09')
ON CONFLICT (email) DO NOTHING;

-- ---------- Subjects ----------
INSERT INTO subjects (name, code) VALUES
    ('Math',       'MATH'),
    ('Portuguese', 'PORT'),
    ('History',    'HIST'),
    ('Physics',    'PHYS'),
    ('Chemistry',  'CHEM')
ON CONFLICT (code) DO NOTHING;

-- ---------- Grades ----------
INSERT INTO grades (student_id, subject_id, grade)
SELECT s.id, sub.id, v.grade
FROM (VALUES
    -- Ana
    ('Ana Souza',    'ana.souza@example.com',    'MATH', 92.50),
    ('Ana Souza',    'ana.souza@example.com',    'PORT', 88.00),
    ('Ana Souza',    'ana.souza@example.com',    'HIST', 75.25),
    ('Ana Souza',    'ana.souza@example.com',    'CHEM', 84.00),

    -- Bruno
    ('Bruno Lima',   'bruno.lima@example.com',   'MATH', 64.00),
    ('Bruno Lima',   'bruno.lima@example.com',   'PHYS', 71.50),
    ('Bruno Lima',   'bruno.lima@example.com',   'CHEM', 69.75),

    -- Carla
    ('Carla Mendes', 'carla.mendes@example.com', 'PORT', 95.00),
    ('Carla Mendes', 'carla.mendes@example.com', 'HIST', 82.75),
    ('Carla Mendes', 'carla.mendes@example.com', 'PHYS', 79.00),
    ('Carla Mendes', 'carla.mendes@example.com', 'CHEM', 91.25),

    -- Diego
    ('Diego Rocha',  'diego.rocha@example.com',  'MATH', 58.50),
    ('Diego Rocha',  'diego.rocha@example.com',  'PHYS', 67.25),
    ('Diego Rocha',  'diego.rocha@example.com',  'CHEM', 62.00)
) AS v(student_name, student_email, subject_code, grade)
JOIN students s ON s.email = v.student_email
               AND s.name  = v.student_name
JOIN subjects sub ON sub.code = v.subject_code
ON CONFLICT (student_id, subject_id) DO NOTHING;