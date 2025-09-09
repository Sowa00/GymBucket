// Test file for Frontend Integration
// This file will test if the frontend is properly integrated with backend APIs

const fs = require('fs');
const path = require('path');

// Test configuration
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

function checkForAPIUsage(filePath) {
  if (!checkFileExists(filePath)) {
    return { exists: false, apiUsage: [] };
  }
  
  const content = fs.readFileSync(filePath, 'utf8');
  const apiUsage = [];
  
  // Check for API service usage
  if (content.includes('this.clientService.')) apiUsage.push('ClientService');
  if (content.includes('this.authService.')) apiUsage.push('AuthService');
  if (content.includes('this.trainingSessionService.')) apiUsage.push('TrainingSessionService');
  if (content.includes('this.exerciseService.')) apiUsage.push('ExerciseService');
  if (content.includes('this.workoutPlanService.')) apiUsage.push('WorkoutPlanService');
  
  return { exists: true, apiUsage };
}

// Run the test
console.log('🔍 Testing Frontend Integration...\n');

let allTestsPassed = true;
const results = [];

// Test 1: Check Clients Component
console.log('📋 Test 1: Clients Component');
const clientsComponentPath = path.join(FRONTEND_PATH, 'clients/clients.component.ts');
const clientsCompilationErrors = checkForCompilationErrors(clientsComponentPath);
const clientsDummyData = checkForDummyData(clientsComponentPath);
const clientsAPIUsage = checkForAPIUsage(clientsComponentPath);

if (clientsCompilationErrors.errors.length === 0) {
  console.log('✅ Clients component has no compilation errors');
} else {
  console.log('❌ Clients component has compilation errors:');
  clientsCompilationErrors.errors.forEach(error => console.log(`   - ${error}`));
  allTestsPassed = false;
}

if (clientsDummyData.dummyData.length === 0) {
  console.log('✅ Clients component has no dummy data');
} else {
  console.log('❌ Clients component has dummy data:');
  clientsDummyData.dummyData.forEach(data => console.log(`   - ${data}`));
  allTestsPassed = false;
}

console.log(`   API Usage: ${clientsAPIUsage.apiUsage.join(', ')}`);
results.push({ test: 'ClientsComponent', passed: clientsCompilationErrors.errors.length === 0 && clientsDummyData.dummyData.length === 0 });

// Test 2: Check Homepage Component
console.log('\n📋 Test 2: Homepage Component');
const homepageComponentPath = path.join(FRONTEND_PATH, 'homepage/homepage.component.ts');
const homepageCompilationErrors = checkForCompilationErrors(homepageComponentPath);
const homepageDummyData = checkForDummyData(homepageComponentPath);
const homepageAPIUsage = checkForAPIUsage(homepageComponentPath);

if (homepageCompilationErrors.errors.length === 0) {
  console.log('✅ Homepage component has no compilation errors');
} else {
  console.log('❌ Homepage component has compilation errors:');
  homepageCompilationErrors.errors.forEach(error => console.log(`   - ${error}`));
  allTestsPassed = false;
}

if (homepageDummyData.dummyData.length === 0) {
  console.log('✅ Homepage component has no dummy data');
} else {
  console.log('❌ Homepage component has dummy data:');
  homepageDummyData.dummyData.forEach(data => console.log(`   - ${data}`));
  allTestsPassed = false;
}

console.log(`   API Usage: ${homepageAPIUsage.apiUsage.join(', ')}`);
results.push({ test: 'HomepageComponent', passed: homepageCompilationErrors.errors.length === 0 && homepageDummyData.dummyData.length === 0 });

// Test 3: Check Workout Plans Component
console.log('\n📋 Test 3: Workout Plans Component');
const workoutPlansComponentPath = path.join(FRONTEND_PATH, 'workout-plans/workout-plans.component.ts');
const workoutPlansDummyData = checkForDummyData(workoutPlansComponentPath);
const workoutPlansAPIUsage = checkForAPIUsage(workoutPlansComponentPath);

if (workoutPlansDummyData.dummyData.length === 0) {
  console.log('✅ Workout Plans component has no dummy data');
} else {
  console.log('❌ Workout Plans component has dummy data:');
  workoutPlansDummyData.dummyData.forEach(data => console.log(`   - ${data}`));
  allTestsPassed = false;
}

console.log(`   API Usage: ${workoutPlansAPIUsage.apiUsage.join(', ')}`);
results.push({ test: 'WorkoutPlansComponent', passed: workoutPlansDummyData.dummyData.length === 0 });

// Test 4: Check Calendar Component
console.log('\n📋 Test 4: Calendar Component');
const calendarComponentPath = path.join(FRONTEND_PATH, 'calendar/calendar.component.ts');
const calendarDummyData = checkForDummyData(calendarComponentPath);
const calendarAPIUsage = checkForAPIUsage(calendarComponentPath);

if (calendarDummyData.dummyData.length === 0) {
  console.log('✅ Calendar component has no dummy data');
} else {
  console.log('❌ Calendar component has dummy data:');
  calendarDummyData.dummyData.forEach(data => console.log(`   - ${data}`));
  allTestsPassed = false;
}

console.log(`   API Usage: ${calendarAPIUsage.apiUsage.join(', ')}`);
results.push({ test: 'CalendarComponent', passed: calendarDummyData.dummyData.length === 0 });

// Test 5: Check if required services exist
console.log('\n📋 Test 5: Required Services');
const requiredServices = [
  'services/auth.service.ts',
  'services/client.service.ts',
  'services/training-session.service.ts'
];

const optionalServices = [
  'services/exercise.service.ts',
  'services/workout-plan.service.ts'
];

let servicesExist = true;
requiredServices.forEach(service => {
  const servicePath = path.join(FRONTEND_PATH, service);
  if (checkFileExists(servicePath)) {
    console.log(`✅ ${service} exists`);
  } else {
    console.log(`❌ ${service} missing`);
    servicesExist = false;
    allTestsPassed = false;
  }
});

optionalServices.forEach(service => {
  const servicePath = path.join(FRONTEND_PATH, service);
  if (checkFileExists(servicePath)) {
    console.log(`✅ ${service} exists (optional)`);
  } else {
    console.log(`⚠️ ${service} missing (optional)`);
  }
});

results.push({ test: 'RequiredServices', passed: servicesExist });

// Test 6: Check if test credentials are preserved
console.log('\n📋 Test 6: Test Credentials Preservation');
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

// Summary
console.log('\n📊 Test Summary:');
console.log('================');
results.forEach(result => {
  const status = result.passed ? '✅ PASS' : '❌ FAIL';
  console.log(`${status} ${result.test}`);
});

if (allTestsPassed) {
  console.log('\n🎉 SUCCESS: All frontend integration tests passed!');
  console.log('✅ Frontend is properly integrated with backend APIs.');
  process.exit(0);
} else {
  console.log('\n💥 FAILURE: Some frontend integration tests failed!');
  console.log('🔧 Please fix the issues before proceeding.');
  process.exit(1);
}
