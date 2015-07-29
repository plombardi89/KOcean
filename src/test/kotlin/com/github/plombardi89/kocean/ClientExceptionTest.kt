package com.github.plombardi89.kocean

import com.github.plombardi89.kocean.model.HostAndPort
import org.junit.Test
import org.junit.Assert.*


class ClientExceptionTest {

  Test fun noProxySpecified_invalidResponseReceivedMessageDoesNotContainProxyInformation() {
    val configWithoutProxy = KOceanConfig("foobar")

    val exception = ClientException.invalidResponse(configWithoutProxy)
    assertEquals("Invalid response (host: api.digitalocean.com:443)", exception.getMessage())
  }

  Test fun invalidResponseReceivedMessageIsFormattedCorrectlyWhenUsingAProxy() {
    val configWithProxy = KOceanConfig(apiToken = "foobar", proxyHost = HostAndPort("localhost", 8033))

    val exception = ClientException.invalidResponse(configWithProxy)
    assertEquals("Invalid response (host: api.digitalocean.com:443, proxy: localhost:8033)", exception.getMessage())
  }
}