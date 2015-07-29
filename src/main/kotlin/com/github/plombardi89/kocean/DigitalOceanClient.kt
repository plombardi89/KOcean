package com.github.plombardi89.kocean

import com.eclipsesource.json.JsonObject
import com.eclipsesource.json.JsonValue
import com.github.plombardi89.kocean.model.RateLimited
import java.io.*
import java.net.*
import java.time.Instant
import javax.net.ssl.HttpsURLConnection


class DigitalOceanClient(val config: KOceanConfig) {

  private val mediaTypeAndEncoding = "application/json; charset=utf-8"

  public data class Result(val status: Int, val json: JsonObject?)

  fun get(path: String, parameters: Map<String, String> = mapOf(), page: Int = 1, pageSize: Int = config.pageSize): Result {
    return executeRequest(
        method = "GET",
        path = path,
        query = mapOf("page" to page.toString(), "per_page" to pageSize.toString()) + parameters)
  }

  fun post(path: String, body: JsonValue): Result {
    return executeRequest(
        method = "POST",
        path = path,
        headers = mapOf("Content-Type" to mediaTypeAndEncoding), body = body.toString())
  }

  fun put(path: String, body: JsonValue): Result {
    return executeRequest(
        method = "PUT",
        path = path,
        headers = mapOf("Content-Type" to mediaTypeAndEncoding), body = body.toString())
  }

  fun delete(path: String): Result {
    return executeRequest(method = "DELETE", path = path)
  }

  private fun executeRequest(
      method: String,
      path: String,
      query: Map<String, String> = mapOf(),
      headers: Map<String, String> = mapOf(),
      body: String? = null
  ): Result {

    val conn = createApiHttpsURLConnection(path, query)

    conn.setRequestProperty("Authorization", "Bearer ${config.apiToken}")
    for (header in headers) {
      conn.addRequestProperty(header.getKey(), header.getValue())
    }

    var result: BufferedReader? = null
    var status: Int? = null
    try {
      conn.setRequestMethod(method)

      if (body != null) {
        conn.setDoOutput(true)
        OutputStreamWriter(conn.getOutputStream()).use {
          it.write(body)
        }
      }

      val responseData = conn.getInputStream()
      status = conn.getResponseCode()

      when(status) {
        in 200..201 -> {
          val (contentType, charset) = parseContentTypeAndCharset(conn.getHeaderField("Content-Type"))
          result = responseData.bufferedReader(charset)
        }
      }
    } catch (ioe: IOException) {
      // NOT FOUND - 404 is "special" because it indicates the absence of a resource and is not really a client failure
      // like many of the other status codes.
      if (status != 404) {
        when(status) {
          429 -> throw RateLimited(Instant.now())
          in 400..499 -> {
            val (contentType, charset) = parseContentTypeAndCharset(conn.getHeaderField("Content-Type"))
            if (contentType == "application/json") {
              val errorData = conn.getErrorStream()
              val json = errorData.bufferedReader(charset).use { JsonObject.readFrom(it) }
              throw ClientException("${json.get("message").asString()} (id: ${json.get("id").asString()})")
            } else {
              throw ClientException("An unknown error occurred during communication with the server")
            }
          }
          else -> {
            throw RuntimeException("Todo: Service exception")
          }
        }
      }
    }

    return Result(conn.getResponseCode(), result?.use { JsonObject.readFrom(it) })
  }

  private fun parseContentTypeAndCharset(contentTypeHeader: String): Pair<String, String> {
    val parts = contentTypeHeader.split(';')
    val contentType = parts[0].trim()

    for (part in parts) {
      val temp = part.trim()
      if (temp.toLowerCase().startsWith("charset=")) {
        return Pair(contentType, temp.substring("charset=".length()))
      }
    }

    return Pair(contentType, "utf-8")
  }

  private fun parseCharsetFromContentType(contentType: String): String {
    val parts = contentType.split(';')
    var charset = ""

    for (part in parts) {
      val temp = part.trim()
      if (temp.toLowerCase().startsWith("charset=")) {
        return temp.substring("charset=".length())
      }
    }

    return if (charset != "") charset else "utf-8"
  }

  private fun createApiHttpsURLConnection(path: String, parameters: Map<String, String>): HttpsURLConnection {
    val queryString = parameters.entrySet().joinToString(separator="&")

    val uri = URI("https", null, config.host.host, config.host.port, createApiPath(path), queryString, null)
    val url = uri.toURL()

    val conn = if (config.proxyHost == null) {
      url.openConnection()
    } else {
      url.openConnection(Proxy(Proxy.Type.HTTP, InetSocketAddress(config.proxyHost.host, config.proxyHost.port)))
    }

    return conn as HttpsURLConnection
  }

  private fun createApiPath(path: String): String {
    return "/v2/" + if (path.startsWith('/')) {
      path.substring(path.indexOf('/') + 1)
    } else {
      path
    }
  }
}