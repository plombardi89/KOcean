package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class Region(val id: String, val name: String, val available: Boolean) {
  constructor(json: JsonObject): this(
      id        = json.get("slug").asString(),
      name      = json.get("name").asString(),
      available = json.get("available").asBoolean()
  )
}