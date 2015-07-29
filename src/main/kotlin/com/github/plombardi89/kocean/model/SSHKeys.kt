package com.github.plombardi89.kocean.model


public data class SSHKeys(private val items: Map<String, SSHKey>, meta: Meta, links: Links):
    Resources(meta, links), Map<String, SSHKey> by items, Sequence<SSHKey> by items.values().asSequence()