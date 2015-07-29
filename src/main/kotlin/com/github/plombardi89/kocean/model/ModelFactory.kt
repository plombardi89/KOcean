package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public interface ModelFactory<T> {
  fun fromJson(json: JsonObject): T
}