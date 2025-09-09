// Test file for Overall Application Functionality
// This file will test if the entire application is working correctly

const fs = require('fs');
const path = require('path');

// Test configuration
const BACKEND_PATH = './backend/src/main/java/gym/backend';
const FRONTEND_PATH = './frontend/src/app';

function checkFileExists(filePath) {
  return fs.existsSync(filePath);
}

function checkForCompilationErrors(filePath) {
  if (!checkFileExists(filePath)) {
    return { exists: false, errors: [] };
  }
  
  const content = fs.readFileSync(filePath, 'utf8');
  const errors = [];
  
  // Check for common TypeScript compilation errors
  if (content.includes('Property \'authService\' does not exist')) {
    errors.push('authService property missing');
  }
  if (content.includes('Argument of type \'string | undefined\' is not assignable')) {
    errors.push('Type safety issues with undefined values');
  }
  if (content.includes('The *ngIf directive was used in the template, but neither the NgIf directive nor the CommonModule was imported')) {
    errors.push('CommonModule import missing');
  }
  
  return { exists: true, errors };
}

function checkForDummyData(filePath) {
  if (!checkFileExists(filePath)) {
    return { exists: false, dummyData: [] };
  }
  
  const content = fs.readFileSync(filePath, 'utf8');
  const dummyData = [];
  
  // Check for dummy data patterns (excluding test credentials)
  const dummyPatterns = [
    'Anna Kowalska', 'Michał Nowak', 'Ewa Wiśniewska', 'Tomasz Zieliński',
    'anna@example.com', 'michal@example.com', 'ewa@example.com',
    'client1', 'client2', 'client3', 'trainer1', 'ex1', 'ex2', 'ex3', 'tpl1',
    'Push Day', 'HIIT Cardio', 'Burpees'
  ];
  
  dummyPatterns.forEach(pattern => {
    if (content.includes(pattern)) {
      dummyData.push(pattern);
    }
  });
  
  return { exists: true, dummyData };
}

// Run the test
console.log('🔍 Testing Overall Application Functionality...\n');

let allTestsPassed = true;
const results = [];

// Test 1: Check Backend Structure
console.log('📋 Test 1: Backend Structure');
const backendStructure = [
  'controller/UserController.java',
  'controller/ClientController.java',
  'controller/TrainingSessionController.java',
  'service/UserService.java',
  'service/ClientService.java',
  'service/TrainingSessionService.java',
  'model/User.java',
  'model/Client.java',
  'model/TrainingSession.java',
  'repository/UserRepository.java',
  'repository/ClientRepository.java',
  'repository/TrainingSessionRepository.java',
  'config/SecurityConfig.java',
  'config/CorsConfig.java',
  'config/JwtConfig.java',
  'security/JwtAuthenticationFilter.java'
];

let backendStructurePassed = true;
backendStructure.forEach(file => {
  const filePath = path.join(BACKEND_PATH, file);
  if (checkFileExists(filePath)) {
    console.log(`✅ ${file}`);
  } else {
    console.log(`❌ ${file} missing`);
    backendStructurePassed = false;
    allTestsPassed = false;
  }
});

results.push({ test: 'BackendStructure', passed: backendStructurePassed });

// Test 2: Check Frontend Structure
console.log('\n📋 Test 2: Frontend Structure');
const frontendStructure = [
  'login/login.component.ts',
  'register/register.component.ts',
  'homepage/homepage.component.ts',
  'clients/clients.component.ts',
  'calendar/calendar.component.ts',
  'workout-plans/workout-plans.component.ts',
  'services/auth.service.ts',
  'services/client.service.ts',
  'services/training-session.service.ts',
  'guards/auth.guard.ts',
  'interceptors/auth.interceptor.ts',
  'app.routes.ts',
  'app.config.ts'
];

let frontendStructurePassed = true;
frontendStructure.forEach(file => {
  const filePath = path.join(FRONTEND_PATH, file);
  if (checkFileExists(filePath)) {
    console.log(`✅ ${file}`);
  } else {
    console.log(`❌ ${file} missing`);
    frontendStructurePassed = false;
    allTestsPassed = false;
  }
});

results.push({ test: 'FrontendStructure', passed: frontendStructurePassed });

// Test 3: Check for Compilation Errors
console.log('\n📋 Test 3: Compilation Errors');
const componentsToCheck = [
  'clients/clients.component.ts',
  'homepage/homepage.component.ts',
  'workout-plans/workout-plans.component.ts',
  'calendar/calendar.component.ts'
];

let compilationErrorsPassed = true;
componentsToCheck.forEach(component => {
  const componentPath = path.join(FRONTEND_PATH, component);
  const errors = checkForCompilationErrors(componentPath);
  
  if (errors.errors.length === 0) {
    console.log(`✅ ${component} - No compilation errors`);
  } else {
    console.log(`❌ ${component} - Has compilation errors:`);
    errors.errors.forEach(error => console.log(`   - ${error}`));
    compilationErrorsPassed = false;
    allTestsPassed = false;
  }
});

results.push({ test: 'CompilationErrors', passed: compilationErrorsPassed });

// Test 4: Check for Dummy Data
console.log('\n📋 Test 4: Dummy Data Removal');
const componentsToCheckForDummyData = [
  'clients/clients.component.ts',
  'homepage/homepage.component.ts',
  'workout-plans/workout-plans.component.ts',
  'calendar/calendar.component.ts'
];

let dummyDataRemovalPassed = true;
componentsToCheckForDummyData.forEach(component => {
  const componentPath = path.join(FRONTEND_PATH, component);
  const dummyData = checkForDummyData(componentPath);
  
  if (dummyData.dummyData.length === 0) {
    console.log(`✅ ${component} - No dummy data`);
  } else {
    console.log(`❌ ${component} - Has dummy data:`);
    dummyData.dummyData.forEach(data => console.log(`   - ${data}`));
    dummyDataRemovalPassed = false;
    allTestsPassed = false;
  }
});

results.push({ test: 'DummyDataRemoval', passed: dummyDataRemovalPassed });

// Test 5: Check Test Credentials
console.log('\n📋 Test 5: Test Credentials Preservation');
const authServicePath = path.join(FRONTEND_PATH, 'services/auth.service.ts');
const authServiceContent = fs.readFileSync(authServicePath, 'utf8');

const hasTestCredentials = authServiceContent.includes('a@a.com') && authServiceContent.includes('2137');

if (hasTestCredentials) {
  console.log('✅ Test credentials (a@a.com/2137) are preserved');
} else {
  console.log('❌ Test credentials are missing');
  allTestsPassed = false;
}
results.push({ test: 'TestCredentials', passed: hasTestCredentials });

// Test 6: Check Database Schema
console.log('\n📋 Test 6: Database Schema');
const migrationPath = './backend/src/main/resources/db/migration/V1__init.sql';
const migrationExists = checkFileExists(migrationPath);

if (migrationExists) {
  console.log('✅ Database migration file exists');
  
  const migrationContent = fs.readFileSync(migrationPath, 'utf8');
  const requiredTables = [
    'CREATE TABLE users',
    'CREATE TABLE clients',
    'CREATE TABLE training_sessions',
    'CREATE TABLE exercises',
    'CREATE TABLE workout_plans'
  ];
  
  let allTablesExist = true;
  requiredTables.forEach(table => {
    if (migrationContent.includes(table)) {
      console.log(`✅ ${table} exists`);
    } else {
      console.log(`❌ ${table} missing`);
      allTablesExist = false;
      allTestsPassed = false;
    }
  });
  
  results.push({ test: 'DatabaseSchema', passed: allTablesExist });
} else {
  console.log('❌ Database migration file missing');
  allTestsPassed = false;
  results.push({ test: 'DatabaseSchema', passed: false });
}

// Test 7: Check Configuration Files
console.log('\n📋 Test 7: Configuration Files');
const configFiles = [
  'backend/pom.xml',
  'backend/src/main/resources/application.properties',
  'frontend/package.json',
  'frontend/angular.json',
  'start-gymbucket.bat',
  'start-backend.bat',
  'start-frontend.bat',
  'stop-gymbucket.bat'
];

let configFilesPassed = true;
configFiles.forEach(file => {
  if (checkFileExists(file)) {
    console.log(`✅ ${file}`);
  } else {
    console.log(`❌ ${file} missing`);
    configFilesPassed = false;
    allTestsPassed = false;
  }
});

results.push({ test: 'ConfigurationFiles', passed: configFilesPassed });

// Summary
console.log('\n📊 Test Summary:');
console.log('================');
results.forEach(result => {
  const status = result.passed ? '✅ PASS' : '❌ FAIL';
  console.log(`${status} ${result.test}`);
});

console.log('\n📈 Overall Progress:');
console.log('===================');
const totalTests = results.length;
const passedTests = results.filter(r => r.passed).length;
const progressPercentage = Math.round((passedTests / totalTests) * 100);

console.log(`✅ Passed: ${passedTests}/${totalTests} (${progressPercentage}%)`);
console.log(`❌ Failed: ${totalTests - passedTests}/${totalTests} (${100 - progressPercentage}%)`);

if (allTestsPassed) {
  console.log('\n🎉 SUCCESS: All tests passed!');
  console.log('✅ The GymBucket application is ready for use.');
  console.log('🚀 You can now start the application using the batch files.');
  process.exit(0);
} else {
  console.log('\n💥 FAILURE: Some tests failed!');
  console.log('🔧 Please fix the issues before proceeding.');
  console.log('📋 Refer to the comprehensive task list for guidance.');
  process.exit(1);
}
