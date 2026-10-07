package com.olha.ca1

import java.util.concurrent.atomic.AtomicLong

class PlacedMarkList {
    private val placedMarks = ArrayList<PlacedMark>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<PlacedMark> {
        return placedMarks
    }

    fun create(placedMark: PlacedMark) {
        placedMark.id = lastId.incrementAndGet()
        placedMarks.add(placedMark)
    }

    fun update(placedMark: PlacedMark): Boolean {
        val found = findOne(placedMark.id)
        return if (found != null) {
            found.title = placedMark.title
            found.desc = placedMark.desc
            found.x = placedMark.x
            found.y = placedMark.y
            true
        } else {
            false
        }
    }

    fun delete(id: Long): Boolean {
        val found = findOne(id)
        return if (found != null) {
            placedMarks.remove(found)
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): PlacedMark? {
        return placedMarks.find { p -> p.id == id }
    }
}
