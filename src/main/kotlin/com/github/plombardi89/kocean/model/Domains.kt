package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class Domains(private val items: Map<String, Domain>, meta: Meta, links: Links):
    Map<String, Domain> by items, Resources(meta, links) {

  companion object: ModelFactory<Domains> {
    override fun fromJson(json: JsonObject): Domains {
      val domains = json.get("domains").asArray()
      val (meta, links) = readMetaAndLinksData(json)
      val mappedDomains = domains.values().toMap(
          { it.asObject().get("domain").asString() }, { Domain.fromJson(it.asObject()) })

      return Domains(mappedDomains, meta, links)
    }
  }
}