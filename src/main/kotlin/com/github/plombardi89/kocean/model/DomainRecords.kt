package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class DomainRecords(private val items: Map<String, Domain>, meta: Meta, links: Links):
    Map<String, Domain> by items, Resources(meta, links) {

  companion object: ModelFactory<DomainRecords> {
    override fun fromJson(json: JsonObject): DomainRecords {
      val records = json.get("domain_records").asArray()
      val (meta, links) = readMetaAndLinksData(json)
      val mappedRecords = records.values().toMap(
          { it.asObject().get("domain").asString() }, { Domain.fromJson(it.asObject()) })

      return DomainRecords(mappedRecords, meta, links)
    }
  }
}