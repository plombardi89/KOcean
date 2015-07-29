package com.github.plombardi89.kocean.model


public enum class ImageRole(val alias: String) {

  SNAPSHOT("snapshot"), TEMPORARY("temporary"), BACKUP("backup"), UNKNOWN("");

  companion object {
    private val aliases = ImageRole.values().toMap { it.alias }

    fun fromAlias(alias: String?): ImageRole {
      if (alias == null) {
        return UNKNOWN
      }

      return aliases.getOrElse(alias, { ImageRole.UNKNOWN })
    }
  }
}