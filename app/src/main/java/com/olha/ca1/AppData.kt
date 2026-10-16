package com.olha.ca1

// sample trails were taken from https://www.sportireland.ie/outdoors/walking/trails
 
object AppData {
    val trails = TrailList()
    var loadedFromApi = false

    init {
        trails.create(
            Trail(
                name = "Barrow Way",
                description = "Long-distance walking trail along the River Barrow corridor.",
                location = "County Carlow",
                difficulty = "Easy",
                distance = 116.9,
                rating = 0.0
            )
        )
        trails.create(
            Trail(
                name = "Grand Canal Way",
                description = "Waymarked walking route following the Grand Canal.",
                location = "County Dublin",
                difficulty = "Easy",
                distance = 117.1,
                rating = 0.0
            )
        )
        trails.create(
            Trail(
                name = "Ballyhoura Way",
                description = "Multi-day walking route through the Ballyhoura region.",
                location = "County Cork",
                difficulty = "Moderate",
                distance = 89.0,
                rating = 0.0
            )
        )
        trails.create(
            Trail(
                name = "Burren Way",
                description = "Trail through the Burren landscape, including coastal sections near the Cliffs of Moher.",
                location = "County Clare",
                difficulty = "Moderate",
                distance = 118.8,
                rating = 0.0
            )
        )
        trails.create(
            Trail(
                name = "Sligo Way",
                description = "Multi-day waymarked walking trail in County Sligo.",
                location = "County Sligo",
                difficulty = "Moderate",
                distance = 78.5,
                rating = 0.0
            )
        )
        trails.create(
            Trail(
                name = "Croagh Patrick Heritage Trail",
                description = "Heritage walking trail linked with Croagh Patrick in County Mayo.",
                location = "County Mayo",
                difficulty = "Strenuous",
                distance = 59.9,
                rating = 0.0
            )
        )
        trails.create(
            Trail(
                name = "Kerry Way",
                description = "Long-distance waymarked walking trail around the Iveragh Peninsula.",
                location = "County Kerry",
                difficulty = "Strenuous",
                distance = 232.1,
                rating = 0.0
            )
        )
    }
}
