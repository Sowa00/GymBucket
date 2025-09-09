-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    isActive BOOLEAN NOT NULL DEFAULT TRUE,
    isEmailVerified BOOLEAN NOT NULL DEFAULT FALSE,
    emailVerificationToken VARCHAR(255),
    resetPasswordToken VARCHAR(255),
    resetPasswordTokenExpiry TIMESTAMP NULL,
    experience INT,
    avatar VARCHAR(500),
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lastLogin TIMESTAMP NULL
);

-- Specializations
CREATE TABLE IF NOT EXISTS user_specializations (
    user_id BIGINT NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    CONSTRAINT fk_user_specializations_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Certifications
CREATE TABLE IF NOT EXISTS user_certifications (
    user_id BIGINT NOT NULL,
    certification VARCHAR(100) NOT NULL,
    CONSTRAINT fk_user_certifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Clients table
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    dateOfBirth DATE,
    gender VARCHAR(10),
    height DOUBLE,
    weight DOUBLE,
    medicalConditions VARCHAR(500),
    fitnessGoals VARCHAR(500),
    notes VARCHAR(500),
    isActive BOOLEAN NOT NULL DEFAULT TRUE,
    trainer_id BIGINT NOT NULL,
    startDate DATE,
    lastSessionDate DATE,
    totalSessions INT DEFAULT 0,
    monthlyFee DOUBLE,
    paymentStatus VARCHAR(20) DEFAULT 'PENDING',
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clients_trainer FOREIGN KEY (trainer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Training Sessions table
CREATE TABLE IF NOT EXISTS training_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sessionDate DATE NOT NULL,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    sessionType VARCHAR(50) NOT NULL,
    location VARCHAR(500),
    notes VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    client_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    price DOUBLE,
    isPaid BOOLEAN DEFAULT FALSE,
    feedback VARCHAR(1000),
    rating INT,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_training_sessions_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_training_sessions_trainer FOREIGN KEY (trainer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Exercises table
CREATE TABLE IF NOT EXISTS exercises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    category VARCHAR(20) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    instructions VARCHAR(2000),
    tips VARCHAR(500),
    warnings VARCHAR(500),
    imageUrl VARCHAR(200),
    videoUrl VARCHAR(200),
    isCustom BOOLEAN DEFAULT FALSE,
    created_by BIGINT,
    isPublic BOOLEAN DEFAULT TRUE,
    usageCount INT DEFAULT 0,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_exercises_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Workout Plans table
CREATE TABLE IF NOT EXISTS workout_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    category VARCHAR(20) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    estimatedDuration INT,
    created_by BIGINT NOT NULL,
    isPublic BOOLEAN DEFAULT FALSE,
    isTemplate BOOLEAN DEFAULT FALSE,
    usageCount INT DEFAULT 0,
    rating INT DEFAULT 0,
    ratingCount INT DEFAULT 0,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_workout_plans_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Workout Plan Exercises table
CREATE TABLE IF NOT EXISTS workout_plan_exercises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    workout_plan_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    orderIndex INT NOT NULL,
    sets INT,
    reps VARCHAR(50),
    weight DOUBLE,
    duration INT,
    restTime INT,
    notes VARCHAR(500),
    isOptional BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_workout_plan_exercises_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE,
    CONSTRAINT fk_workout_plan_exercises_exercise FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE CASCADE
);

-- Client Emergency Contacts
CREATE TABLE IF NOT EXISTS client_emergency_contacts (
    client_id BIGINT NOT NULL,
    contact_info VARCHAR(200) NOT NULL,
    CONSTRAINT fk_client_emergency_contacts_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

-- Exercise Muscle Groups
CREATE TABLE IF NOT EXISTS exercise_muscle_groups (
    exercise_id BIGINT NOT NULL,
    muscle_group VARCHAR(50) NOT NULL,
    CONSTRAINT fk_exercise_muscle_groups_exercise FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE CASCADE
);

-- Exercise Equipment
CREATE TABLE IF NOT EXISTS exercise_equipment (
    exercise_id BIGINT NOT NULL,
    equipment VARCHAR(50) NOT NULL,
    CONSTRAINT fk_exercise_equipment_exercise FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE CASCADE
);

-- Workout Plan Muscle Groups
CREATE TABLE IF NOT EXISTS workout_plan_muscle_groups (
    workout_plan_id BIGINT NOT NULL,
    muscle_group VARCHAR(50) NOT NULL,
    CONSTRAINT fk_workout_plan_muscle_groups_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE
);

-- Workout Plan Equipment
CREATE TABLE IF NOT EXISTS workout_plan_equipment (
    workout_plan_id BIGINT NOT NULL,
    equipment VARCHAR(50) NOT NULL,
    CONSTRAINT fk_workout_plan_equipment_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE
);

-- Workout Plan Tags
CREATE TABLE IF NOT EXISTS workout_plan_tags (
    workout_plan_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL,
    CONSTRAINT fk_workout_plan_tags_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_clients_email ON clients(email);
CREATE INDEX idx_clients_trainer ON clients(trainer_id);
CREATE INDEX idx_training_sessions_date ON training_sessions(sessionDate);
CREATE INDEX idx_training_sessions_client ON training_sessions(client_id);
CREATE INDEX idx_training_sessions_trainer ON training_sessions(trainer_id);
CREATE INDEX idx_exercises_category ON exercises(category);
CREATE INDEX idx_exercises_difficulty ON exercises(difficulty);
CREATE INDEX idx_workout_plans_category ON workout_plans(category);
CREATE INDEX idx_workout_plans_created_by ON workout_plans(created_by);

-- Insert default test user (password: 2137)
INSERT INTO users (email, password, firstName, lastName, role, isActive, isEmailVerified, createdAt, updatedAt) 
VALUES ('a@a.com', '$2a$10$YHFMSamUwcrP4yjhUsv/meS0IB.FHICmp7oZsVGmqf5xMma2hr0GW', 'Jan', 'Kowalski', 'TRAINER', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert test specializations for the default user
INSERT INTO user_specializations (user_id, specialization) 
VALUES (1, 'Siłownia'), (1, 'Kardio'), (1, 'Dietetyka');

-- Insert test certifications for the default user
INSERT INTO user_certifications (user_id, certification) 
VALUES (1, 'ACE Personal Trainer'), (1, 'NASM-CPT');

