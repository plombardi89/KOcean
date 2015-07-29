package com.github.plombardi89.kocean

import java.nio.charset.Charset


open class IntegrationTest {

  protected val apiToken: String
  protected val digitalOcean: KOcean

  init {
    apiToken = readFixture("DIGITALOCEAN_API_TOKEN")
    digitalOcean = KOcean(apiToken)
  }

  protected fun readFixture(fixture: String, charset: Charset = Charsets.UTF_8): String {
    return javaClass.getClassLoader().getResourceAsStream(fixture).reader(charset).readText()
  }
}