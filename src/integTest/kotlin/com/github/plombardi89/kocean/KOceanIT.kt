package com.github.plombardi89.kocean

import com.github.plombardi89.kocean.model.SSHKey
import org.junit.After
import org.junit.Test
import org.junit.Assert.*


class KOceanIT: IntegrationTest() {

  private val testSSHKeyName = "insecure_test_key"
  private val testSSHKeyFingerprint = readFixture("${testSSHKeyName}.fingerprint").trim()

  Test fun getAccountInfo_ReturnsAccountInfo() {
    val accountInfo = digitalOcean.getAccountInfo()
    assertNotNull("ID is present (not empty or null)", accountInfo.id)
    assertEquals("Email address", "plombardi89@gmail.com", accountInfo.email)
    assertTrue("Email is verified", accountInfo.emailVerified)
  }

  Test fun createGetThenDeleteSSHKey() {
    val publicKey = readFixture("${testSSHKeyName}.pub")
    val newKey = digitalOcean.createSSHKey(name = testSSHKeyName, publicKey = publicKey)
    assertTrue(newKey.id.isNotBlank())
    assertEquals(publicKey, newKey.publicKey)
    assertEquals(testSSHKeyName, newKey.name)
    assertEquals(testSSHKeyFingerprint, newKey.fingerprint)

    digitalOcean.renameSSHKey(newKey.id, "${testSSHKeyName}_renamed")

    var foundKey = digitalOcean.getSSHKey(newKey.id)
    assertEquals(newKey.id, foundKey?.id)
    assertEquals(newKey.fingerprint, foundKey?.fingerprint)
    assertEquals("${testSSHKeyName}_renamed", foundKey?.name)
    assertEquals(newKey.publicKey, foundKey?.publicKey)

    digitalOcean.deleteSSHKey("${foundKey?.id}")
    foundKey = digitalOcean.getSSHKey(newKey.id)
    assertNull(foundKey)
  }

  Test fun getSizes() {
    var sizes = digitalOcean.getDropletSizes(page = 1, pageSize = 5)
    assertTrue(sizes.size() == 5)
  }
}