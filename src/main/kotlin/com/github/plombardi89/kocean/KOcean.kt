package com.github.plombardi89.kocean

import com.eclipsesource.json.JsonObject
import com.github.plombardi89.kocean.model.*
import java.time.Instant
import java.util.*


public class KOcean(val config: KOceanConfig) {

  private val digitalOcean: DigitalOceanClient

  init {
    digitalOcean = DigitalOceanClient(config)
  }

  constructor(token: String): this(KOceanConfig(token))

  // -------------------------------------------------------------------------------------------------------------------
  // Account Information
  // -------------------------------------------------------------------------------------------------------------------

  fun getAccountInfo(): Account {
    val result = digitalOcean.get("/account")
    if (result.json != null) {
      return Account(result.json)
    } else {
      throw ClientException("No valid data returned from server")
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Actions
  // -------------------------------------------------------------------------------------------------------------------

//  fun getAction(id: String): Action {
//    val json = digitalOcean.get("/actions/${id}").get("action").asObject()
//    return Action(json.get("id").asInt().toString(),
//             ActionStatus.COMPLETED,
//             json.get("type").asString(),
//             Instant.parse(json.get("started_at").asString()),
//             Instant.parse(json.get("completed_at").asString()),
//             json.get("region_slug").asString(),
//             json.get("resource_type").asString(),
//             json.get("resource_id").asString())
//  }

  // -------------------------------------------------------------------------------------------------------------------
  // Domains
  // -------------------------------------------------------------------------------------------------------------------

  fun createDomain(name: String, ipAddress: String): Domain {
    val requestJson = JsonObject().add("name", name).add("ip_address", ipAddress)
    val result = digitalOcean.post("/domains/", requestJson)
    return Domain.fromJson(result.json ?: throw ClientException.invalidResponse(config))
  }

  fun deleteDomain(domain: String) {
    digitalOcean.delete("/domains/${domain}")
  }

  fun getDomain(domain: String): Domain {
    val result = digitalOcean.get(path = "/domains/${domain}")
    if (result.json != null) {
      return Domain.fromJson(result.json)
    } else {
      throw ClientException("No valid data returned from server")
    }
  }

  fun getDomainRecord(domain: String, recordId: String): DomainRecord {
    val result = digitalOcean.get(path = "/domains/${domain}/record/${recordId}")
    if (result.json != null) {
      return DomainRecord.fromJson(result.json)
    } else {
      throw ClientException("No valid data returned from server")
    }
  }

  fun getDomains(page: Int = 1, pageSize: Int = config.pageSize): Domains {
    val result = digitalOcean.get(path = "/domains", page = page, pageSize = pageSize)
    if (result.json != null) {
      return Domains.fromJson(result.json)
    } else {
      throw ClientException.invalidResponse(config)
    }
  }

  fun getDomainRecords(domain: String, page: Int = 1, pageSize: Int = config.pageSize): DomainRecords {
    val result = digitalOcean.get(path = "/domains/${domain}/records", page = page, pageSize = pageSize)
    if (result.json != null) {
      return DomainRecords.fromJson(result.json)
    } else {
      throw ClientException.invalidResponse(config)
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // SSH Key Management
  // -------------------------------------------------------------------------------------------------------------------

  fun createSSHKey(name: String, publicKey: String): SSHKey {
    val requestJson = JsonObject().add("name", name).add("public_key", publicKey)
    val result = digitalOcean.post("/account/keys", requestJson)
    return SSHKey(result.json ?: throw ClientException.invalidResponse(config))
  }

  fun deleteSSHKey(id: String) {
    digitalOcean.delete("/account/keys/${id}")
  }

  fun getSSHKey(id: String): SSHKey? {
    val result = digitalOcean.get("/account/keys/${id}")
    if (result.status != 404) {
      return SSHKey(result.json ?: throw ClientException.invalidResponse(config))
    } else {
      return null
    }
  }

  fun getSSHKeys(page: Int = 1, pageSize: Int = config.pageSize): SSHKeys {
    val result = digitalOcean.get(path = "/account/keys", page = page, pageSize = pageSize)
    val json = result.json
    if (json != null) {
      val keys = json.get("ssh_keys").asArray()
      val meta = Meta(json.get("meta").asObject())
      val links = Links(json.get("links").asObject())
      val mappedKeys = keys.values().toMap({ it.asObject().get("id").asString() }, { SSHKey(it.asObject()) })

      return SSHKeys(mappedKeys, meta, links)
    } else {
      throw ClientException.invalidResponse(config)
    }
  }

  fun renameSSHKey(id: String, newName: String): SSHKey {
    val requestJson  = JsonObject().add("name", newName)
    val result = digitalOcean.put("/account/keys/${id}", requestJson)

    if (result.status != 404) {
      return SSHKey(result.json ?: throw ClientException.invalidResponse(config))
    } else {
      throw ClientException("Cannot rename nonexistent SSH key (id or fingerprint: ${id})")
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Regions
  // -------------------------------------------------------------------------------------------------------------------

  fun getRegions(page: Int = 1, pageSize: Int = config.pageSize): Regions {
    val result = digitalOcean.get(path = "/regions", page = page, pageSize = pageSize)

    val json = result.json
    if (json != null) {
      val regionsArray = json.get("regions").asArray()
      val meta = Meta(json.get("meta").asObject())
      val links = Links(json.get("links").asObject())
      val mappedRegions = regionsArray
          .values()
          .toMap({ it.asObject().get("slug").asString() }, { Region(it.asObject()) })

      return Regions(mappedRegions, meta, links)
    } else {
      throw ClientException.invalidResponse(config)
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Images
  // -------------------------------------------------------------------------------------------------------------------

  fun deleteImage(id: String) {
    digitalOcean.delete("/images/${id}")
  }

  fun getImage(id: String): Image {
    val result = digitalOcean.get(path = "/images/${id}")
    if (result.json != null) {
      return Image.fromJson(result.json)
    } else {
      throw ClientException("No valid data returned from server")
    }
  }

  fun getImages(type: String? = null, privateOnly: Boolean = false, page: Int = 1, pageSize: Int = config.pageSize): Images {
    val parameters = linkedMapOf<String, String>()
    if (privateOnly) {
      parameters.put("private", "true")
    }

    if (type != null) {
      parameters.put("type", type)
    }

    val result = digitalOcean.get(path = "/images", parameters = parameters, page = page, pageSize = pageSize)

    if (result.json != null) {
      return Images.fromJson(result.json)
    } else {
      throw ClientException.invalidResponse(config)
    }
  }

  fun renameImage(id: String, newName: String): Image {
    val requestJson = JsonObject().add("name", newName)
    val result = digitalOcean.post("/images/${id}", requestJson)
    return Image.fromJson(result.json ?: throw ClientException.invalidResponse(config))
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Image Actions
  // -------------------------------------------------------------------------------------------------------------------

  fun convertToSnapshot(imageId: String): ImageAction {
    val requestJson = JsonObject().add("type", "convert")
    val result = digitalOcean.post("/images/${imageId}/actions", requestJson)
    return ImageAction.fromJson(result.json ?: throw ClientException.invalidResponse(config))
  }

  fun getImageAction(imageId: String, actionId: String): ImageAction {
    val result = digitalOcean.get(path = "/images/${imageId}/actions/${actionId}")
    if (result.json != null) {
      return ImageAction.fromJson(result.json)
    } else {
      throw ClientException("No valid data returned from server")
    }
  }

  fun getImageActions(imageId: String, page: Int = 1, pageSize: Int = config.pageSize): ImageActions {
    val result = digitalOcean.get(path = "/images/${imageId}/actions", page = page, pageSize = pageSize)
    if (result.json != null) {
      return ImageActions.fromJson(result.json)
    } else {
      throw ClientException.invalidResponse(config)
    }
  }

  fun transferImage(imageId: String, destination: String): ImageAction {
    val requestJson = JsonObject().add("type", "transfer").add("region", destination)
    val result = digitalOcean.post("/images/${imageId}/actions", requestJson)
    return ImageAction.fromJson(result.json ?: throw ClientException.invalidResponse(config))
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Droplet Sizes
  // -------------------------------------------------------------------------------------------------------------------

  fun getDropletSizes(page: Int = 1, pageSize: Int = config.pageSize): DropletSizes {
    val result = digitalOcean.get(path = "/sizes", page = page, pageSize = pageSize)
    if (result.json != null) {
      return DropletSizes.fromJson(result.json)
    } else {
      throw ClientException.invalidResponse(config)
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Droplets
  // -------------------------------------------------------------------------------------------------------------------
}