package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class ImageActions(private val items: Map<String, ImageAction>, meta: Meta, links: Links):
    Map<String, ImageAction> by items, Resources(meta, links) {

  companion object: ModelFactory<ImageActions> {
    override fun fromJson(json: JsonObject): ImageActions {
      val actions = json.get("action").asArray()
      val (meta, links) = readMetaAndLinksData(json)
      val mappedActins = actions.values().toMap(
          { it.asObject().get("domain").asString() }, { ImageAction.fromJson(it.asObject()) })

      return ImageActions(mappedActins, meta, links)
    }
  }
}