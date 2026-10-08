const { initializeTestEnvironment, assertFails, assertSucceeds } = require('@firebase/rules-unit-testing');
const { test, before, after, beforeEach } = require('node:test');
const fs = require('node:fs');

const PROJECT_ID = 'demo-no-project';
const DATABASE_ID = 'ai-studio-android-9aa3d647-a398-4b19-8cd1-9383cff619f5';
let testEnv;

before(async () => {
  const rules = fs.readFileSync('firestore.rules', 'utf8');
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: '127.0.0.1',
      port: 8085
    }
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test('Unauthenticated user cannot read or write work records', async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore({ databaseId: DATABASE_ID });
  const ref = unauthedDb.collection('work_records').doc('TOC-101');
  await assertFails(ref.get());
  await assertFails(ref.set({
    token: 'TOC-101',
    name: 'Customer',
    phone: '9800000001',
    service: 'PAN Card',
    stage: 1,
    userId: 'any_user',
    createdAt: new Date()
  }));
});

test('Authenticated user can read and create valid work records', async () => {
  const authedDb = testEnv.authenticatedContext('user_123').firestore({ databaseId: DATABASE_ID });
  const ref = authedDb.collection('work_records').doc('TOC-101');
  
  await assertSucceeds(ref.set({
    token: 'TOC-101',
    name: 'Dilshad',
    phone: '7478654044',
    service: 'PVC Card',
    stage: 1,
    userId: 'user_123',
    createdAt: new Date()
  }));

  await assertSucceeds(ref.get());
});

test('Creation fails if required field is missing or invalid stage', async () => {
  const authedDb = testEnv.authenticatedContext('user_123').firestore({ databaseId: DATABASE_ID });
  const ref = authedDb.collection('work_records').doc('TOC-102');
  
  // Invalid stage (must be 1, 2, or 3)
  await assertFails(ref.set({
    token: 'TOC-102',
    name: 'Test',
    phone: '7478654044',
    service: 'PVC Card',
    stage: 99,
    userId: 'user_123',
    createdAt: new Date()
  }));
});
