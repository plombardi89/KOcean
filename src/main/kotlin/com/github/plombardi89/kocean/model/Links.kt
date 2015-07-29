package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class Links(val first: String?, val previous: String?, val next: String?, val last: String?) {
  constructor(json: JsonObject): this(
      json.getString("first", null),
      json.getString("prev", null),
      json.getString("next", null),
      json.getString("last", null)
  )

  fun isEmpty(): Boolean {
    return first == null && previous == null && next == null && last == null
  }
}