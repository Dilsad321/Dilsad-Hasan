package com.example

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class ShopRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun unauthenticated_write_fails_security_rules() = runBlocking {
        auth.signOut()
        val docRef = firestore.collection("work_records").document("TOC-999")
        val data = hashMapOf(
            "token" to "TOC-999",
            "name" to "Unauthenticated User",
            "phone" to "9876543210",
            "service" to "PAN Card",
            "stage" to 1,
            "userId" to "anonymous",
            "createdAt" to FieldValue.serverTimestamp()
        )

        try {
            docRef.set(data).await()
            fail("Expected PERMISSION_DENIED when unauthenticated")
        } catch (e: Exception) {
            assertTrue(
                "Expected PERMISSION_DENIED but got: ${e.message}",
                e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED
            )
        }
    }

    @Test
    fun authenticated_user_can_create_and_read_work_record() = runBlocking {
        val uid = signInTestUser("customer@example.com")
        val token = "TOC-888"
        val docRef = firestore.collection("work_records").document(token)
        val data = hashMapOf(
            "token" to token,
            "name" to "Dilshad Hasan",
            "phone" to "7478654044",
            "service" to "PVC Card Print",
            "stage" to 2,
            "userId" to uid,
            "createdAt" to FieldValue.serverTimestamp()
        )

        docRef.set(data).await()

        val snapshot = docRef.get().await()
        assertTrue(snapshot.exists())
        assertEquals(token, snapshot.getString("token"))
        assertEquals("Dilshad Hasan", snapshot.getString("name"))
        assertEquals(2L, snapshot.getLong("stage"))
    }
}
