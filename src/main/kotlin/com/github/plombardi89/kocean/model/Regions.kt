package com.github.plombardi89.kocean.model


public class Regions(val regions: Map<String, Region>, meta: Meta, links: Links):
    Resources(meta, links), Map<String, Region> by regions, Sequence<Region> by regions.values().asSequence() {

//  override fun isEmpty(): Boolean {
//    return regions.isEmpty()
//  }
//
//  override fun size(): Int {
//    return regions.size()
//  }
}