package com.github.plombardi89.kocean

import com.github.plombardi89.kocean.model.HostAndPort


public data class KOceanConfig(
    val apiToken: String,
    val pageSize: Int = 25,
    val host: HostAndPort = HostAndPort("api.digitalocean.com", 443),
    val proxyHost: HostAndPort? = null
)