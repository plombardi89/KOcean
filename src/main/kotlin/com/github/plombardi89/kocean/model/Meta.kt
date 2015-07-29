package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class Meta(val total: Int) {
  constructor(json: JsonObject): this(json.getInt("total", 0))
}