package com.github.plombardi89.kocean

import com.eclipsesource.json.JsonObject
import java.time.Instant


fun JsonObject.getString(key: String): String {
  val value = get(key)
  return if (value != null) value.asString() else throw jsonValueIsNull(key)
}

fun JsonObject.getInt(key: String): Int {
  val value = get(key)
  return if (value != null) value.asInt() else throw jsonValueIsNull(key)
}

fun JsonObject.getTime(key: String): Instant {
  val value = get(key)
  return if (value != null) Instant.parse(value.asString()) else throw jsonValueIsNull(key)
}

fun JsonObject.getTime(key: String, defaultValue: Instant?): Instant? {
  val value = get(key)
  return if (value != null) Instant.parse(value.asString()) else defaultValue
}

private fun jsonValueIsNull(key: String): ClientException {
  return ClientException("Expected non-null value for JSON value (key: ${key})")
}