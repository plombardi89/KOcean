package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


private fun <T> ModelFactory<T>.readMetaAndLinksData(json: JsonObject): Pair<Meta, Links> {
  return Pair(Meta(json.get("meta").asObject()), Links(json.get("links").asObject()))
}
