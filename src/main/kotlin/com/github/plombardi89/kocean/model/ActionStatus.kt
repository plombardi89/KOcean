package com.github.plombardi89.kocean.model


public enum class ActionStatus(val alias: String) {
  COMPLETE("completed"), ERROR("errored"), IN_PROGRESS("in-progress"), UNKNOWN("unknown");

  companion object {
    private val aliases = ActionStatus.values().toMap { it.alias }

    fun fromAlias(alias: String?): ActionStatus {
      if (alias == null) {
        return ActionStatus.UNKNOWN
      }

      return aliases.getOrElse(alias, { ActionStatus.UNKNOWN })
    }
  }
}