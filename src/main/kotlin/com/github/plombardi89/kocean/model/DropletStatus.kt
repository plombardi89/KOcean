package com.github.plombardi89.kocean.model


public enum class DropletStatus(val alias: String) {

  NEW("new"), ACTIVE("active"), OFF("off"), ARCHIVE("archive");

  companion object {
    private val aliases = DropletStatus.values().toMap { it.alias }

    fun fromAlias(alias: String): DropletStatus {
      return aliases.getOrElse(alias,
                               { throw IllegalArgumentException(
                                   "Unknown Droplet status alias (provided: ${alias}, expected: ${alias})") })
    }
  }
}