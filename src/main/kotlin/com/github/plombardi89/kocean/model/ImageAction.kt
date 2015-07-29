package com.github.plombardi89.kocean.model

import com.eclipsesource.json.JsonObject
import com.github.plombardi89.kocean.getInt
import com.github.plombardi89.kocean.getString
import com.github.plombardi89.kocean.getTime
import java.time.Instant


public data class ImageAction(val id: String, val status: ActionStatus, val type: String, val startTime: Instant?,
                         val completionTime: Instant?, val region: String?, val resourceId: String,
                         val resourceType: String) {

  companion object: ModelFactory<ImageAction> {
    override fun fromJson(json: JsonObject): ImageAction {
      return ImageAction(id             = json.getInt("id").toString(),
                         status         = ActionStatus.fromAlias(json.getString("status")),
                         type           = json.getString("type"),
                         startTime      = json.getTime("started_at", null),
                         completionTime = json.getTime("completed_at", null),
                         region         = json.getString("region_slug", null),
                         resourceId     = json.getString("resource_id"),
                         resourceType   = json.getString("resource_type"))
    }
  }
}