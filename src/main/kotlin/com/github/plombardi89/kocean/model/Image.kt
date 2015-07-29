package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject
import java.time.Instant


public data class Image(val id: String, val slug: String?, val name: String, val distribution: String,
                        val role: ImageRole, val public: Boolean, val minimumDiskSize: Long,
                        val regions: Collection<String>, val creationTime: Instant) {

  companion object: ModelFactory<Image> {
    override fun fromJson(json: JsonObject): Image {
      return Image(id              = json.get("id").asString(),
                   slug            = json.getString("slug", null),
                   name            = json.get("name").asString(),
                   distribution    = json.get("distribution").asString(),
                   role = ImageRole.fromAlias(json.getString("type", null)),
                   public          = json.get("public").asBoolean(),
                   minimumDiskSize = json.get("min_disk_size").asLong(),
                   regions         = json.get("regions").asArray().values().map { it.asString() },
                   creationTime    = Instant.parse(json.get("created_at").asString()))
    }
  }
}