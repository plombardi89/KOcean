package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class DomainRecord(val id: String, val type: String, val name: String, val data: String,
                                val priority: Int?, val weight: Int?, val port: Int?) {

  companion object: ModelFactory<DomainRecord> {
    override fun fromJson(json: JsonObject): DomainRecord {
      return DomainRecord(id       = json.get("id").asInt().toString(),
                           type     = json.get("type").asString(),
                           name     = json.get("name").asString(),
                           data     = json.get("data").asString(),
                           priority = if (!json.get("priority").isNull()) json.get("priority").asInt() else null,
                           weight   = if (!json.get("weight").isNull()) json.get("weight").asInt() else null,
                           port     = if (!json.get("port").isNull()) json.get("port").asInt() else null)
    }
  }
}