package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class Domain(val domain: String, val timeToLive: Int, val zoneFileContents: String) {

  companion object: ModelFactory<Domain> {
    override fun fromJson(json: JsonObject): Domain {
      return Domain(domain           = json.get("domain").asString(),
                     timeToLive       = json.get("ttl").asInt(),
                     zoneFileContents = json.get("zone_file").asString())
    }
  }
}