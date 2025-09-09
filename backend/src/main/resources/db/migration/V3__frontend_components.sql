            -- V3: Frontend Components Database Support
-- This migration adds database support for all frontend components

-- Calendar/Scheduling System
CREATE TABLE IF NOT EXISTS calendar_events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    event_type VARCHAR(50) NOT NULL, -- TRAINING, NUTRITION, MEETING, REMINDER
    start_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_date DATE NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, CONFIRMED, CANCELLED, COMPLETED
    priority VARCHAR(20) DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH, URGENT
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern VARCHAR(50), -- DAILY, WEEKLY, MONTHLY, YEARLY
    recurrence_end_date DATE,
    client_id BIGINT,
    trainer_id BIGINT NOT NULL,
    workout_plan_id BIGINT,
    nutrition_plan_id BIGINT,
    notes VARCHAR(1000),
    reminder_minutes INT DEFAULT 15,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_calendar_events_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_calendar_events_trainer FOREIGN KEY (trainer_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_calendar_events_workout_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE SET NULL,
    CONSTRAINT fk_calendar_events_nutrition_plan FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans(id) ON DELETE SET NULL
);

-- Client Workout Plan Assignments
CREATE TABLE IF NOT EXISTS client_workout_assignments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    workout_plan_id BIGINT NOT NULL,
    assigned_by BIGINT NOT NULL, -- trainer who assigned
    assigned_date DATE NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, COMPLETED, PAUSED, CANCELLED
    notes VARCHAR(500),
    progress_notes VARCHAR(1000),
    completion_percentage INT DEFAULT 0,
    last_workout_date DATE,
    total_workouts_completed INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_workout_assignments_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_workout_assignments_workout_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_workout_assignments_trainer FOREIGN KEY (assigned_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Client Nutrition Plan Assignments
CREATE TABLE IF NOT EXISTS client_nutrition_assignments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    nutrition_plan_id BIGINT NOT NULL,
    assigned_by BIGINT NOT NULL, -- trainer who assigned
    assigned_date DATE NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, COMPLETED, PAUSED, CANCELLED
    notes VARCHAR(500),
    progress_notes VARCHAR(1000),
    completion_percentage INT DEFAULT 0,
    last_meal_logged_date DATE,
    total_meals_logged INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_nutrition_assignments_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_nutrition_assignments_nutrition_plan FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_nutrition_assignments_trainer FOREIGN KEY (assigned_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Client Progress Tracking
CREATE TABLE IF NOT EXISTS client_progress_measurements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    measurement_date DATE NOT NULL,
    weight DOUBLE,
    body_fat_percentage DOUBLE,
    muscle_mass DOUBLE,
    measurements JSON, -- JSON object for body measurements (chest, waist, arms, etc.)
    photos JSON, -- JSON array of photo URLs
    notes VARCHAR(1000),
    measured_by BIGINT NOT NULL, -- trainer who took measurements
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_client_progress_measurements_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_progress_measurements_trainer FOREIGN KEY (measured_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Workout Session Logs
CREATE TABLE IF NOT EXISTS workout_session_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    workout_plan_id BIGINT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    total_duration INT, -- in minutes
    exercises_completed JSON, -- JSON array of completed exercises with sets/reps
    calories_burned INT,
    notes VARCHAR(1000),
    rating INT, -- 1-5 rating of the workout
    difficulty_rating INT, -- 1-5 how difficult it felt
    logged_by BIGINT NOT NULL, -- trainer who logged the session
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_workout_session_logs_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_workout_session_logs_workout_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE,
    CONSTRAINT fk_workout_session_logs_trainer FOREIGN KEY (logged_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Meal Logs
CREATE TABLE IF NOT EXISTS meal_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    meal_id BIGINT NOT NULL,
    nutrition_plan_id BIGINT,
    log_date DATE NOT NULL,
    meal_time TIME NOT NULL,
    portion_size DOUBLE DEFAULT 1.0,
    actual_calories DOUBLE,
    actual_protein DOUBLE,
    actual_carbs DOUBLE,
    actual_fat DOUBLE,
    notes VARCHAR(500),
    logged_by BIGINT NOT NULL, -- trainer who logged the meal
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_meal_logs_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_meal_logs_meal FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    CONSTRAINT fk_meal_logs_nutrition_plan FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans(id) ON DELETE SET NULL,
    CONSTRAINT fk_meal_logs_trainer FOREIGN KEY (logged_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Notifications System
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    type VARCHAR(50) NOT NULL, -- INFO, WARNING, SUCCESS, ERROR, REMINDER
    category VARCHAR(50) NOT NULL, -- TRAINING, NUTRITION, PAYMENT, SYSTEM, REMINDER
    is_read BOOLEAN DEFAULT FALSE,
    is_important BOOLEAN DEFAULT FALSE,
    action_url VARCHAR(500), -- URL to navigate to when clicked
    action_data JSON, -- Additional data for the action
    scheduled_for TIMESTAMP, -- For scheduled notifications
    expires_at TIMESTAMP, -- When notification expires
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- File Uploads System
CREATE TABLE IF NOT EXISTS file_uploads (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    file_type VARCHAR(50) NOT NULL, -- IMAGE, DOCUMENT, VIDEO, AUDIO
    category VARCHAR(50) NOT NULL, -- PROFILE_PHOTO, EXERCISE_IMAGE, PROGRESS_PHOTO, DOCUMENT
    entity_type VARCHAR(50), -- USER, CLIENT, EXERCISE, WORKOUT_PLAN, etc.
    entity_id BIGINT, -- ID of the related entity
    uploaded_by BIGINT NOT NULL,
    is_public BOOLEAN DEFAULT FALSE,
    description VARCHAR(1000),
    client_id BIGINT,
    workout_plan_id BIGINT,
    nutrition_plan_id BIGINT,
    tags VARCHAR(500),
    uploaded_at TIMESTAMP,
    last_accessed_at TIMESTAMP,
    access_count INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_file_uploads_user FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_file_uploads_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_file_uploads_workout_plan FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE,
    CONSTRAINT fk_file_uploads_nutrition_plan FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans(id) ON DELETE CASCADE
);

-- Audit Logging System
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL, -- CREATE, UPDATE, DELETE, LOGIN, LOGOUT, etc.
    entity_type VARCHAR(50) NOT NULL, -- USER, CLIENT, WORKOUT_PLAN, etc.
    entity_id BIGINT,
    old_values JSON, -- Previous values (for updates/deletes)
    new_values JSON, -- New values (for creates/updates)
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    action_type VARCHAR(20), -- CREATE, UPDATE, DELETE, LOGIN, LOGOUT, VIEW, EXPORT, IMPORT
    session_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Dashboard Analytics Cache
CREATE TABLE IF NOT EXISTS dashboard_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    analytics_date DATE NOT NULL,
    total_clients INT DEFAULT 0,
    active_clients INT DEFAULT 0,
    total_sessions_today INT DEFAULT 0,
    total_sessions_this_week INT DEFAULT 0,
    total_sessions_this_month INT DEFAULT 0,
    monthly_revenue NUMERIC(10,2) DEFAULT 0.00,
    completion_rate NUMERIC(5,2) DEFAULT 0.00,
    average_session_rating NUMERIC(3,2) DEFAULT 0.00,
    new_clients_this_month INT DEFAULT 0,
    clients_with_overdue_payments INT DEFAULT 0,
    upcoming_sessions_today INT DEFAULT 0,
    average_meal_rating DECIMAL(3,2) DEFAULT 0.00,
    top_workout_plan VARCHAR(255),
    top_nutrition_plan VARCHAR(255),
    most_active_client VARCHAR(255),
    total_file_uploads INT DEFAULT 0,
    total_meal_logs INT DEFAULT 0,
    total_notifications INT DEFAULT 0,
    total_nutrition_plans INT DEFAULT 0,
    total_workout_plans INT DEFAULT 0,
    unread_notifications INT DEFAULT 0,
    statistics TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dashboard_analytics_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (user_id, analytics_date)
);

-- Client Goals and Milestones
CREATE TABLE IF NOT EXISTS client_goals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    goal_type VARCHAR(50) NOT NULL, -- WEIGHT_LOSS, MUSCLE_GAIN, ENDURANCE, FLEXIBILITY, etc.
    category VARCHAR(50), -- CARDIO, ENDURANCE, FLEXIBILITY, HEALTH, LIFESTYLE, MUSCLE_BUILDING, NUTRITION, OTHER, PERFORMANCE, STRENGTH, WEIGHT_GAIN, WEIGHT_LOSS
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    target_value DOUBLE,
    current_value DOUBLE,
    unit VARCHAR(20), -- kg, %, minutes, etc.
    target_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, COMPLETED, PAUSED, CANCELLED
    priority VARCHAR(20) DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH
    is_public BOOLEAN DEFAULT FALSE,
    progress_notes VARCHAR(1000),
    start_date DATE,
    assigned_by_id BIGINT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    CONSTRAINT fk_client_goals_client FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    CONSTRAINT fk_client_goals_trainer FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_calendar_events_date ON calendar_events(start_date);
CREATE INDEX idx_calendar_events_trainer ON calendar_events(trainer_id);
CREATE INDEX idx_calendar_events_client ON calendar_events(client_id);
CREATE INDEX idx_calendar_events_status ON calendar_events(status);

CREATE INDEX idx_client_workout_assignments_client ON client_workout_assignments(client_id);
CREATE INDEX idx_client_workout_assignments_workout_plan ON client_workout_assignments(workout_plan_id);
CREATE INDEX idx_client_workout_assignments_status ON client_workout_assignments(status);

CREATE INDEX idx_client_nutrition_assignments_client ON client_nutrition_assignments(client_id);
CREATE INDEX idx_client_nutrition_assignments_nutrition_plan ON client_nutrition_assignments(nutrition_plan_id);
CREATE INDEX idx_client_nutrition_assignments_status ON client_nutrition_assignments(status);

CREATE INDEX idx_client_progress_measurements_client ON client_progress_measurements(client_id);
CREATE INDEX idx_client_progress_measurements_date ON client_progress_measurements(measurement_date);

CREATE INDEX idx_workout_session_logs_client ON workout_session_logs(client_id);
CREATE INDEX idx_workout_session_logs_date ON workout_session_logs(session_date);

CREATE INDEX idx_meal_logs_client ON meal_logs(client_id);
CREATE INDEX idx_meal_logs_date ON meal_logs(log_date);

CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_read ON notifications(is_read);
CREATE INDEX idx_notifications_type ON notifications(type);

CREATE INDEX idx_file_uploads_entity ON file_uploads(entity_type, entity_id);
CREATE INDEX idx_file_uploads_category ON file_uploads(category);

CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_date ON audit_logs(created_at);

CREATE INDEX idx_dashboard_analytics_user ON dashboard_analytics(user_id);
CREATE INDEX idx_dashboard_analytics_date ON dashboard_analytics(analytics_date);

CREATE INDEX idx_client_goals_client ON client_goals(client_id);
CREATE INDEX idx_client_goals_status ON client_goals(status);
