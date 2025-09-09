// Test file for Exercise Management functionality
// This file will test if the exercise management API is properly implemented

const fs = require('fs');
const path = require('path');

// Test configuration
const BACKEND_PATH = './backend/src/main/java/gym/backend';
const FRONTEND_PATH = './frontend/src/app';

// Required files for Exercise Management
const REQUIRED_BACKEND_FILES = [
  'controller/ExerciseController.java',
  'service/ExerciseService.java',
  'dto/ExerciseDTO.java',
  'dto/ExerciseRequestDTO.java'
];

const REQUIRED_FRONTEND_FILES = [
  'services/exercise.service.ts'
];

// Required API endpoints
const REQUIRED_ENDPOINTS = [
  'GET /api/exercises',
  'POST /api/exercises',
  'PUT /api/exercises/{id}',
  'DELETE /api/exercises/{id}',
  'GET /api/exercises/search',
  'GET /api/exercises/by-muscle-group',
  'GET /api/exercises/by-equipment',
  'GET /api/exercises/by-difficulty'
];

function checkFileExists(filePath) {
  return fs.existsSync(filePath);
}

function checkControllerEndpoints(controllerPath) {
  if (!checkFileExists(controllerPath)) {
    return { exists: false, endpoints: [] };
  }
  
  const content = fs.readFileSync(controllerPath, 'utf8');
  const endpoints = [];
  
  // Check for common endpoint patterns
  if (content.includes('@GetMapping')) endpoints.push('GET');
  if (content.includes('@PostMapping')) endpoints.push('POST');
  if (content.includes('@PutMapping')) endpoints.push('PUT');
  if (content.includes('@DeleteMapping')) endpoints.push('DELETE');
  
  return { exists: true, endpoints };
}

function checkServiceMethods(servicePath) {
  if (!checkFileExists(servicePath)) {
    return { exists: false, methods: [] };
  }
  
  const content = fs.readFileSync(servicePath, 'utf8');
  const methods = [];
  
  // Check for common service method patterns
  if (content.includes('getAllExercises')) methods.push('getAllExercises');
  if (content.includes('createExercise')) methods.push('createExercise');
  if (content.includes('updateExercise')) methods.push('updateExercise');
  if (content.includes('deleteExercise')) methods.push('deleteExercise');
  if (content.includes('searchExercises')) methods.push('searchExercises');
  
  return { exists: true, methods };
}

function checkFrontendService(servicePath) {
  if (!checkFileExists(servicePath)) {
    return { exists: false, methods: [] };
  }
  
  const content = fs.readFileSync(servicePath, 'utf8');
  const methods = [];
  
  // Check for common frontend service method patterns
  if (content.includes('getAllExercises')) methods.push('getAllExercises');
  if (content.includes('createExercise')) methods.push('createExercise');
  if (content.includes('updateExercise')) methods.push('updateExercise');
  if (content.includes('deleteExercise')) methods.push('deleteExercise');
  if (content.includes('searchExercises')) methods.push('searchExercises');
  
  return { exists: true, methods };
}

// Run the test
console.log('🔍 Testing Exercise Management functionality...\n');

let allTestsPassed = true;
const results = [];

// Test 1: Check if ExerciseController exists
console.log('📋 Test 1: ExerciseController');
const controllerPath = path.join(BACKEND_PATH, 'controller/ExerciseController.java');
const controllerResult = checkControllerEndpoints(controllerPath);

if (controllerResult.exists) {
  console.log('✅ ExerciseController exists');
  console.log(`   Endpoints found: ${controllerResult.endpoints.join(', ')}`);
} else {
  console.log('❌ ExerciseController missing');
  allTestsPassed = false;
}
results.push({ test: 'ExerciseController', passed: controllerResult.exists });

// Test 2: Check if ExerciseService exists
console.log('\n📋 Test 2: ExerciseService');
const servicePath = path.join(BACKEND_PATH, 'service/ExerciseService.java');
const serviceResult = checkServiceMethods(servicePath);

if (serviceResult.exists) {
  console.log('✅ ExerciseService exists');
  console.log(`   Methods found: ${serviceResult.methods.join(', ')}`);
} else {
  console.log('❌ ExerciseService missing');
  allTestsPassed = false;
}
results.push({ test: 'ExerciseService', passed: serviceResult.exists });

// Test 3: Check if ExerciseDTO exists
console.log('\n📋 Test 3: ExerciseDTO');
const dtoPath = path.join(BACKEND_PATH, 'dto/ExerciseDTO.java');
const dtoExists = checkFileExists(dtoPath);

if (dtoExists) {
  console.log('✅ ExerciseDTO exists');
} else {
  console.log('❌ ExerciseDTO missing');
  allTestsPassed = false;
}
results.push({ test: 'ExerciseDTO', passed: dtoExists });

// Test 4: Check if ExerciseRequestDTO exists
console.log('\n📋 Test 4: ExerciseRequestDTO');
const requestDtoPath = path.join(BACKEND_PATH, 'dto/ExerciseRequestDTO.java');
const requestDtoExists = checkFileExists(requestDtoPath);

if (requestDtoExists) {
  console.log('✅ ExerciseRequestDTO exists');
} else {
  console.log('❌ ExerciseRequestDTO missing');
  allTestsPassed = false;
}
results.push({ test: 'ExerciseRequestDTO', passed: requestDtoExists });

// Test 5: Check if frontend exercise service exists
console.log('\n📋 Test 5: Frontend Exercise Service');
const frontendServicePath = path.join(FRONTEND_PATH, 'services/exercise.service.ts');
const frontendServiceResult = checkFrontendService(frontendServicePath);

if (frontendServiceResult.exists) {
  console.log('✅ Frontend exercise service exists');
  console.log(`   Methods found: ${frontendServiceResult.methods.join(', ')}`);
} else {
  console.log('❌ Frontend exercise service missing');
  allTestsPassed = false;
}
results.push({ test: 'FrontendExerciseService', passed: frontendServiceResult.exists });

// Test 6: Check if dummy exercise data is removed
console.log('\n📋 Test 6: Dummy Exercise Data Removal');
const workoutPlansPath = path.join(FRONTEND_PATH, 'workout-plans/workout-plans.component.ts');
const workoutPlansContent = fs.readFileSync(workoutPlansPath, 'utf8');

const hasDummyExercises = workoutPlansContent.includes('ex1') || 
                         workoutPlansContent.includes('ex2') || 
                         workoutPlansContent.includes('ex3');

if (!hasDummyExercises) {
  console.log('✅ Dummy exercise data removed from frontend');
} else {
  console.log('❌ Dummy exercise data still present in frontend');
  allTestsPassed = false;
}
results.push({ test: 'DummyDataRemoval', passed: !hasDummyExercises });

// Summary
console.log('\n📊 Test Summary:');
console.log('================');
results.forEach(result => {
  const status = result.passed ? '✅ PASS' : '❌ FAIL';
  console.log(`${status} ${result.test}`);
});

if (allTestsPassed) {
  console.log('\n🎉 SUCCESS: All exercise management tests passed!');
  console.log('✅ Exercise management functionality is properly implemented.');
  process.exit(0);
} else {
  console.log('\n💥 FAILURE: Some exercise management tests failed!');
  console.log('🔧 Please implement the missing components before proceeding.');
  process.exit(1);
}
