package com.github.plombardi89.kocean.model


public data class HostAndPort(val host: String, val port: Int) {
  init {
    if (port !in (1..65535)) {
      throw IllegalArgumentException("Port is outside allowed range (min: 1, max: 65535, actual: ${port})")
    }

    if (host.isBlank()) {
      throw IllegalArgumentException("Hostname is empty or whitespace")
    }
  }

  public override fun toString(): String {
    return "${host}:${port}"
  }
}