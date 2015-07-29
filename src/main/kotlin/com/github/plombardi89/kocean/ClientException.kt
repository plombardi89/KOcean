package com.github.plombardi89.kocean


public class ClientException(message: String?, cause: Throwable?): RuntimeException(message, cause) {
  constructor(message: String): this(message, null)
  constructor(cause: Throwable): this(null, cause)

  companion object Factory {
    fun invalidResponse(config: KOceanConfig): ClientException {
      val message = "Invalid response " +
          "(host: ${config.host}${if (config.proxyHost != null) ", proxy: ${config.proxyHost}" else "" })"

      return ClientException(message)
    }
  }
}