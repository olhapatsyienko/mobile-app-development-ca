package com.olha.ca1

// data class representing a hiking trail 
 
data class Trail(
    var id: Long = 0L,
    var name: String = "",
    var description: String = "",
    var location: String = "",
    var difficulty: String = "",
    var distance: Double = 0.0,
    var rating: Double = 0.0
)