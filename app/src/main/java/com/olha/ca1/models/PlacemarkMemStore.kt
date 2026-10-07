package com.olha.ca1.models

import java.util.concurrent.atomic.AtomicLong

class PlacemarkMemStore : PlacemarkStore {
    private val placemarks = ArrayList<PlacemarkModel>()
    private val lastId = AtomicLong(0L)

    override fun findAll(): List<PlacemarkModel> {
        return placemarks
    }

    override fun create(placemark: PlacemarkModel) {
        placemark.id = lastId.incrementAndGet()
        placemarks.add(placemark)
    }

    override fun update(placemark: PlacemarkModel): Boolean {
        val foundPlacemark = findOne(placemark.id)
        return if (foundPlacemark != null) {
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            true
        } else {
            false
        }
    }

    override fun delete(id: Long): Boolean {
        val foundPlacemark = findOne(id)
        return if (foundPlacemark != null) {
            placemarks.remove(foundPlacemark)
            true
        } else {
            false
        }
    }

    override fun findOne(id: Long): PlacemarkModel? {
        return placemarks.find { p -> p.id == id }
    }
}
