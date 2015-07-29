package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class Images(private val items: Map<String, Image>, meta: Meta, links: Links):
    Map<String, Image> by items, Resources(meta, links) {

  companion object: ModelFactory<Images> {
    override fun fromJson(json: JsonObject): Images {
      val images = json.get("images").asArray()
      val (meta, links) = readMetaAndLinksData(json)
      val mappedImages = images.values().toMap(
          { it.asObject().get("id").asInt().toString() }, { Image.fromJson(it.asObject()) })

      return Images(mappedImages, meta, links)
    }
  }
}