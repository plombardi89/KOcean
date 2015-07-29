package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject
import java.util.*


public data class Account(val id: String, val email: String, val emailVerified: Boolean, val limits: Map<ResourceLimit, Long>) {

  constructor(json: JsonObject): this(
      json.get("uuid").asString(),
      json.get("email").asString(),
      json.get("email_verified").asBoolean(),
      mapOf())
}