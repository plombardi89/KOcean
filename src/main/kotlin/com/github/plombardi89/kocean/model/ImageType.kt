package com.github.plombardi89.kocean.model


public enum class ImageType(val alias: String) {

  DISTRIBUTION("distribution"), APPLICATION("application"), UNKNOWN("");

  companion object {
    private val aliases = ImageType.values().toMap { it.alias }

    fun fromAlias(alias: String?): ImageType {
      if (alias == null) {
        return UNKNOWN
      }

      return aliases.getOrElse(alias, { ImageType.UNKNOWN })
    }
  }
}