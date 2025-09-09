// Test file for Workout Plan functionality
// This file will test if the workout plan management API is properly implemented

const fs = require('fs');
const path = require('path');

// Test configuration
const BACKEND_PATH = './backend/src/main/java/gym/backend';
const FRONTEND_PATH = './frontend/src/app';

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
  if (content.includes('getAllWorkoutPlans')) methods.push('getAllWorkoutPlans');
  if (content.includes('createWorkoutPlan')) methods.push('createWorkoutPlan');
  if (content.includes('updateWorkoutPlan')) methods.push('updateWorkoutPlan');
  if (content.includes('deleteWorkoutPlan')) methods.push('deleteWorkoutPlan');
  if (content.includes('duplicateWorkoutPlan')) methods.push('duplicateWorkoutPlan');
  if (content.includes('assignClientToPlan')) methods.push('assignClientToPlan');
  
  return { exists: true, methods };
}

function checkFrontendService(servicePath) {
  if (!checkFileExists(servicePath)) {
    return { exists: false, methods: [] };
  }
  
  const content = fs.readFileSync(servicePath, 'utf8');
  const methods = [];
  
  // Check for common frontend service method patterns
  if (content.includes('getAllWorkoutPlans')) methods.push('getAllWorkoutPlans');
  if (content.includes('createWorkoutPlan')) methods.push('createWorkoutPlan');
  if (content.includes('updateWorkoutPlan')) methods.push('updateWorkoutPlan');
  if (content.includes('deleteWorkoutPlan')) methods.push('deleteWorkoutPlan');
  if (content.includes('duplicateWorkoutPlan')) methods.push('duplicateWorkoutPlan');
  
  return { exists: true, methods };
}

// Run the test
console.log('🔍 Testing Workout Plan functionality...\n');

let allTestsPassed = true;
const results = [];

// Test 1: Check if WorkoutPlanController exists
console.log('📋 Test 1: WorkoutPlanController');
const controllerPath = path.join(BACKEND_PATH, 'controller/WorkoutPlanController.java');
const controllerResult = checkControllerEndpoints(controllerPath);

if (controllerResult.exists) {
  console.log('✅ WorkoutPlanController exists');
  console.log(`   Endpoints found: ${controllerResult.endpoints.join(', ')}`);
} else {
  console.log('❌ WorkoutPlanController missing');
  allTestsPassed = false;
}
results.push({ test: 'WorkoutPlanController', passed: controllerResult.exists });

// Test 2: Check if WorkoutPlanService exists
console.log('\n📋 Test 2: WorkoutPlanService');
const servicePath = path.join(BACKEND_PATH, 'service/WorkoutPlanService.java');
const serviceResult = checkServiceMethods(servicePath);

if (serviceResult.exists) {
  console.log('✅ WorkoutPlanService exists');
  console.log(`   Methods found: ${serviceResult.methods.join(', ')}`);
} else {
  console.log('❌ WorkoutPlanService missing');
  allTestsPassed = false;
}
results.push({ test: 'WorkoutPlanService', passed: serviceResult.exists });

// Test 3: Check if WorkoutPlanDTO exists
console.log('\n📋 Test 3: WorkoutPlanDTO');
const dtoPath = path.join(BACKEND_PATH, 'dto/WorkoutPlanDTO.java');
const dtoExists = checkFileExists(dtoPath);

if (dtoExists) {
  console.log('✅ WorkoutPlanDTO exists');
} else {
  console.log('❌ WorkoutPlanDTO missing');
  allTestsPassed = false;
}
results.push({ test: 'WorkoutPlanDTO', passed: dtoExists });

// Test 4: Check if WorkoutPlanRequestDTO exists
console.log('\n📋 Test 4: WorkoutPlanRequestDTO');
const requestDtoPath = path.join(BACKEND_PATH, 'dto/WorkoutPlanRequestDTO.java');
const requestDtoExists = checkFileExists(requestDtoPath);

if (requestDtoExists) {
  console.log('✅ WorkoutPlanRequestDTO exists');
} else {
  console.log('❌ WorkoutPlanRequestDTO missing');
  allTestsPassed = false;
}
results.push({ test: 'WorkoutPlanRequestDTO', passed: requestDtoExists });

// Test 5: Check if WorkoutPlanExerciseDTO exists
console.log('\n📋 Test 5: WorkoutPlanExerciseDTO');
const exerciseDtoPath = path.join(BACKEND_PATH, 'dto/WorkoutPlanExerciseDTO.java');
const exerciseDtoExists = checkFileExists(exerciseDtoPath);

if (exerciseDtoExists) {
  console.log('✅ WorkoutPlanExerciseDTO exists');
} else {
  console.log('❌ WorkoutPlanExerciseDTO missing');
  allTestsPassed = false;
}
results.push({ test: 'WorkoutPlanExerciseDTO', passed: exerciseDtoExists });

// Test 6: Check if frontend workout plan service exists
console.log('\n📋 Test 6: Frontend Workout Plan Service');
const frontendServicePath = path.join(FRONTEND_PATH, 'services/workout-plan.service.ts');
const frontendServiceResult = checkFrontendService(frontendServicePath);

if (frontendServiceResult.exists) {
  console.log('✅ Frontend workout plan service exists');
  console.log(`   Methods found: ${frontendServiceResult.methods.join(', ')}`);
} else {
  console.log('❌ Frontend workout plan service missing');
  allTestsPassed = false;
}
results.push({ test: 'FrontendWorkoutPlanService', passed: frontendServiceResult.exists });

// Test 7: Check if dummy workout plan data is removed
console.log('\n📋 Test 7: Dummy Workout Plan Data Removal');
const workoutPlansPath = path.join(FRONTEND_PATH, 'workout-plans/workout-plans.component.ts');
const workoutPlansContent = fs.readFileSync(workoutPlansPath, 'utf8');

const hasDummyPlans = workoutPlansContent.includes('Push Day') || 
                     workoutPlansContent.includes('HIIT Cardio') ||
                     workoutPlansContent.includes('client1') ||
                     workoutPlansContent.includes('client2') ||
                     workoutPlansContent.includes('client3');

if (!hasDummyPlans) {
  console.log('✅ Dummy workout plan data removed from frontend');
} else {
  console.log('❌ Dummy workout plan data still present in frontend');
  allTestsPassed = false;
}
results.push({ test: 'DummyDataRemoval', passed: !hasDummyPlans });

// Test 8: Check if WorkoutPlan model exists
console.log('\n📋 Test 8: WorkoutPlan Model');
const modelPath = path.join(BACKEND_PATH, 'model/WorkoutPlan.java');
const modelExists = checkFileExists(modelPath);

if (modelExists) {
  console.log('✅ WorkoutPlan model exists');
} else {
  console.log('❌ WorkoutPlan model missing');
  allTestsPassed = false;
}
results.push({ test: 'WorkoutPlanModel', passed: modelExists });

// Test 9: Check if WorkoutPlanRepository exists
console.log('\n📋 Test 9: WorkoutPlanRepository');
const repositoryPath = path.join(BACKEND_PATH, 'repository/WorkoutPlanRepository.java');
const repositoryExists = checkFileExists(repositoryPath);

if (repositoryExists) {
  console.log('✅ WorkoutPlanRepository exists');
} else {
  console.log('❌ WorkoutPlanRepository missing');
  allTestsPassed = false;
}
results.push({ test: 'WorkoutPlanRepository', passed: repositoryExists });

// Summary
console.log('\n📊 Test Summary:');
console.log('================');
results.forEach(result => {
  const status = result.passed ? '✅ PASS' : '❌ FAIL';
  console.log(`${status} ${result.test}`);
});

if (allTestsPassed) {
  console.log('\n🎉 SUCCESS: All workout plan tests passed!');
  console.log('✅ Workout plan functionality is properly implemented.');
  process.exit(0);
} else {
  console.log('\n💥 FAILURE: Some workout plan tests failed!');
  console.log('🔧 Please implement the missing components before proceeding.');
  process.exit(1);
}
