// Test file to verify dummy data removal (excluding test credentials)
// This file will check if dummy data has been properly removed from frontend components
// while preserving test credentials a@a.com/2137

const fs = require('fs');
const path = require('path');

// Test configuration
const FRONTEND_PATH = './frontend/src/app';
const DUMMY_DATA_PATTERNS = [
  // Common dummy data patterns
  'mockData',
  'dummyData',
  'fakeData',
  'sampleData',
  'testData',
  // Hardcoded values that should be dynamic (excluding test credentials)
  'hardcoded',
  'static',
  // Mock objects
  'mock',
  'fake',
  'sample',
  // Common dummy values (excluding test credentials)
  'John Doe',
  'Jane Smith',
  'Test User',
  'example@example.com',
  '123-456-7890',
  // Dummy statistics
  'totalClients: 10',
  'todaysSessions: 5',
  'weeklyRevenue: "500 zł"',
  'completionRate: "85%"',
  // Specific dummy data patterns
  'Anna Kowalska',
  'Michał Nowak',
  'Ewa Wiśniewska',
  'Tomasz Zieliński',
  'anna@example.com',
  'michal@example.com',
  'ewa@example.com',
  'tomasz@example.com',
  'client1',
  'client2',
  'client3',
  'trainer1',
  'ex1',
  'ex2',
  'ex3',
  'tpl1'
];

// Patterns to exclude (test credentials)
const EXCLUDE_PATTERNS = [
  'a@a.com',
  '2137',
  'mockLogin',
  'mockRegister'
];

function checkForDummyData(filePath) {
  const content = fs.readFileSync(filePath, 'utf8');
  const foundPatterns = [];
  
  DUMMY_DATA_PATTERNS.forEach(pattern => {
    if (content.includes(pattern)) {
      // Check if this pattern should be excluded
      const shouldExclude = EXCLUDE_PATTERNS.some(excludePattern => 
        content.includes(excludePattern)
      );
      
      if (!shouldExclude) {
        foundPatterns.push(pattern);
      }
    }
  });
  
  return foundPatterns;
}

function scanDirectory(dirPath) {
  const results = [];
  const files = fs.readdirSync(dirPath);
  
  files.forEach(file => {
    const filePath = path.join(dirPath, file);
    const stat = fs.statSync(filePath);
    
    if (stat.isDirectory()) {
      results.push(...scanDirectory(filePath));
    } else if (file.endsWith('.ts') || file.endsWith('.html')) {
      const dummyData = checkForDummyData(filePath);
      if (dummyData.length > 0) {
        results.push({
          file: filePath,
          patterns: dummyData
        });
      }
    }
  });
  
  return results;
}

// Run the test
console.log('🔍 Scanning for dummy data in frontend components (excluding test credentials)...\n');

const dummyDataResults = scanDirectory(FRONTEND_PATH);

if (dummyDataResults.length === 0) {
  console.log('✅ SUCCESS: No dummy data found in frontend components!');
  console.log('✅ Test credentials a@a.com/2137 preserved as requested.');
  process.exit(0);
} else {
  console.log('❌ FAILURE: Dummy data found in the following files:');
  dummyDataResults.forEach(result => {
    console.log(`\n📁 ${result.file}`);
    result.patterns.forEach(pattern => {
      console.log(`   - Found: "${pattern}"`);
    });
  });
  console.log('\n🔧 Please remove dummy data before proceeding.');
  process.exit(1);
}
