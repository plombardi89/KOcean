package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject
import java.math.BigDecimal


public data class DropletSize(
    val slug: String,
    val available: Boolean,
    val transferBandwidth: BigDecimal,
    val monthlyPrice: BigDecimal,
    val hourlyPrice: BigDecimal,
    val processors: Int,
    val memory: Long,
    val diskSize: Long,
    val regions: Collection<String>
) {

  companion object Factory {
    fun fromJson(json: JsonObject): DropletSize {
      return DropletSize(
          slug              = json.get("slug").asString(),
          available         = json.get("available").asBoolean(),
          transferBandwidth = BigDecimal(json.get("transfer").asDouble().toString()),
          monthlyPrice      = BigDecimal(json.get("price_monthly").asDouble().toString()),
          hourlyPrice       = BigDecimal(json.get("price_hourly").asDouble().toString()),
          processors        = json.get("vcpus").asInt(),
          memory            = json.get("memory").asLong(),
          diskSize          = json.get("disk").asLong(),
          regions           = json.get("regions").asArray().map { it.asString() }
      )
    }
  }
}

public data class DropletSizes(private val items: Map<String, DropletSize>, meta: Meta, links: Links):
    Resources(meta, links), Map<String, DropletSize> by items, Sequence<DropletSize> by items.values().asSequence() {

  companion object Factory {
    fun fromJson(json: JsonObject): DropletSizes {
      val sizes = json.get("sizes").asArray()
      val meta = Meta(json.get("meta").asObject())
      val links = Links(json.get("links").asObject())
      val mappedSizes = sizes.values()
          .toMap({ it.asObject().get("slug").asString() }, { DropletSize.fromJson(it.asObject()) })

      return DropletSizes(mappedSizes, meta, links)
    }
  }
}