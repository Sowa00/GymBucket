-- Nutrition Plans table
CREATE TABLE IF NOT EXISTS nutrition_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    category VARCHAR(20) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    targetCalories INT,
    targetProtein INT,
    targetCarbs INT,
    targetFat INT,
    duration INT, -- in days
    created_by BIGINT NOT NULL,
    isPublic BOOLEAN DEFAULT FALSE,
    isTemplate BOOLEAN DEFAULT FALSE,
    usageCount INT DEFAULT 0,
    rating INT DEFAULT 0,
    ratingCount INT DEFAULT 0,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_nutrition_plans_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Meals table
CREATE TABLE IF NOT EXISTS meals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    category VARCHAR(20) NOT NULL, -- BREAKFAST, LUNCH, DINNER, SNACK
    calories INT,
    protein DOUBLE,
    carbs DOUBLE,
    fat DOUBLE,
    fiber DOUBLE,
    sugar DOUBLE,
    sodium DOUBLE,
    instructions VARCHAR(2000),
    prepTime INT, -- in minutes
    cookTime INT, -- in minutes
    servings INT DEFAULT 1,
    isCustom BOOLEAN DEFAULT FALSE,
    created_by BIGINT,
    isPublic BOOLEAN DEFAULT TRUE,
    usageCount INT DEFAULT 0,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_meals_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Nutrition Plan Meals table
CREATE TABLE IF NOT EXISTS nutrition_plan_meals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nutrition_plan_id BIGINT NOT NULL,
    meal_id BIGINT NOT NULL,
    dayNumber INT NOT NULL,
    mealOrder INT NOT NULL, -- 1=breakfast, 2=lunch, 3=dinner, 4=snack
    portionSize DOUBLE DEFAULT 1.0, -- multiplier for the meal
    notes VARCHAR(500),
    isOptional BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_nutrition_plan_meals_plan FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans(id) ON DELETE CASCADE,
    CONSTRAINT fk_nutrition_plan_meals_meal FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE
);

-- Meal Ingredients table
CREATE TABLE IF NOT EXISTS meal_ingredients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    meal_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    unit VARCHAR(20) NOT NULL, -- g, ml, cup, tbsp, tsp, piece, etc.
    calories DOUBLE,
    protein DOUBLE,
    carbs DOUBLE,
    fat DOUBLE,
    fiber DOUBLE,
    sugar DOUBLE,
    sodium DOUBLE,
    CONSTRAINT fk_meal_ingredients_meal FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE
);

-- Nutrition Plan Categories
CREATE TABLE IF NOT EXISTS nutrition_plan_categories (
    nutrition_plan_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    CONSTRAINT fk_nutrition_plan_categories_plan FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans(id) ON DELETE CASCADE
);

-- Nutrition Plan Tags
CREATE TABLE IF NOT EXISTS nutrition_plan_tags (
    nutrition_plan_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL,
    CONSTRAINT fk_nutrition_plan_tags_plan FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans(id) ON DELETE CASCADE
);

-- Indexes (MySQL compatible - no IF NOT EXISTS)
CREATE INDEX idx_nutrition_plans_category ON nutrition_plans(category);
CREATE INDEX idx_nutrition_plans_created_by ON nutrition_plans(created_by);
CREATE INDEX idx_meals_category ON meals(category);
CREATE INDEX idx_meals_created_by ON meals(created_by);
CREATE INDEX idx_nutrition_plan_meals_plan ON nutrition_plan_meals(nutrition_plan_id);
CREATE INDEX idx_nutrition_plan_meals_day ON nutrition_plan_meals(dayNumber);
CREATE INDEX idx_meal_ingredients_meal ON meal_ingredients(meal_id);

-- Insert sample meals
INSERT INTO meals (name, description, category, calories, protein, carbs, fat, fiber, sugar, sodium, instructions, prepTime, cookTime, servings, isCustom, isPublic, createdAt, updatedAt) VALUES
('Owsianka z owocami', 'Zdrowa owsianka z bananem i jagodami', 'BREAKFAST', 350, 12, 65, 8, 10, 25, 150, '1. Zagotuj wodę z mlekiem\n2. Dodaj płatki owsiane\n3. Gotuj 5 minut\n4. Dodaj owoce', 5, 10, 1, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kurczak z ryżem', 'Grillowany kurczak z brązowym ryżem i warzywami', 'LUNCH', 450, 35, 45, 12, 6, 8, 400, '1. Grilluj kurczaka\n2. Ugotuj ryż\n3. Przygotuj warzywa na parze', 15, 25, 1, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sałatka z tuńczykiem', 'Sałatka z tuńczykiem, jajkiem i warzywami', 'DINNER', 320, 28, 15, 18, 8, 12, 600, '1. Przygotuj sałatę\n2. Dodaj tuńczyka i jajko\n3. Polej sosem', 10, 0, 1, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jabłko z orzechami', 'Jabłko z garścią orzechów włoskich', 'SNACK', 180, 4, 25, 8, 6, 20, 5, '1. Umyj jabłko\n2. Pokrój na kawałki\n3. Dodaj orzechy', 2, 0, 1, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample nutrition plan
INSERT INTO nutrition_plans (name, description, category, difficulty, targetCalories, targetProtein, targetCarbs, targetFat, duration, created_by, isPublic, isTemplate, createdAt, updatedAt) VALUES
('Plan na redukcję', 'Plan żywieniowy na redukcję masy ciała', 'WEIGHT_LOSS', 'INTERMEDIATE', 1800, 120, 180, 80, 30, 1, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Plan na masę', 'Plan żywieniowy na budowanie masy mięśniowej', 'MUSCLE_GAIN', 'INTERMEDIATE', 2500, 150, 300, 100, 30, 1, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
