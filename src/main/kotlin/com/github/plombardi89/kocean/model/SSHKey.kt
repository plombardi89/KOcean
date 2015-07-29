package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject


public data class SSHKey(val id: String, val fingerprint: String, val publicKey: String, val name: String) {
  constructor(json: JsonObject): this(
      json.get("id").asString(),
      json.get("fingerprint").asString(),
      json.get("public_key").asString(),
      json.get("name").asString())
}