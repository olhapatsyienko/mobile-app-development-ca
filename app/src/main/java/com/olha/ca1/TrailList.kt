package com.olha.ca1

import java.util.concurrent.atomic.AtomicLong

class TrailList {
    private val trails = ArrayList<Trail>()
    private val lastId = AtomicLong(0L) 

    fun findAll(): List<Trail> {
        return trails
    }

    fun create(trail: Trail) {
        trail.id = lastId.incrementAndGet()
        trails.add(trail)
    }

    fun replaceAll(newTrails: List<Trail>) {
        val incoming = newTrails.toList()
        trails.clear()
        lastId.set(0L)
        incoming.forEach { create(it) }
    }

    fun update(trail: Trail): Boolean {
        val found = findOne(trail.id)
        return if (found != null) {
            found.name = trail.name
            found.description = trail.description
            found.location = trail.location
            found.difficulty = trail.difficulty
            found.distance = trail.distance
            found.rating = trail.rating
            true
        } else {
            false
        }
    }

    fun delete(id: Long): Boolean {
        val found = findOne(id)
        return if (found != null) {
            trails.remove(found)
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): Trail? {
        return trails.find { t -> t.id == id }
    }
}
