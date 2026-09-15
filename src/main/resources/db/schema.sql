CREATE TABLE IF NOT EXISTS students (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    birth_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS subjects (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(150) NOT NULL UNIQUE,
    code       VARCHAR(20)  NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS grades (
    id         BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    subject_id BIGINT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
    grade      NUMERIC(5,2) NOT NULL CHECK (grade >= 0 AND grade <= 100),
    graded_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_grades_student_subject UNIQUE (student_id, subject_id)
);

CREATE INDEX IF NOT EXISTS idx_grades_student_id ON grades(student_id);
CREATE INDEX IF NOT EXISTS idx_grades_subject_id ON grades(subject_id);