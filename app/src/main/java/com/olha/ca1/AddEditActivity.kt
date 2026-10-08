package com.olha.ca1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var difficultyInput: EditText
    private lateinit var distanceInput: EditText
    private lateinit var ratingInput: RatingBar

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        nameInput = findViewById(R.id.nameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        locationInput = findViewById(R.id.locationInput)
        difficultyInput = findViewById(R.id.difficultyInput)
        distanceInput = findViewById(R.id.distanceInput)
        ratingInput = findViewById(R.id.ratingInput)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        editingId = intent.getLongExtra("id", -1L).takeIf { it != -1L }

        if (editingId != null) {
            findViewById<TextView>(R.id.formTitle).text = "Edit Trail"
            loadExistingTrail(editingId!!)
        }

        saveButton.setOnClickListener { saveTrail() }
        cancelButton.setOnClickListener { finish() }
    }

    private fun loadExistingTrail(id: Long) {
        val trail = AppData.trails.findOne(id)

        if (trail == null) {
            Toast.makeText(this, "Trail not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        nameInput.setText(trail.name)
        descriptionInput.setText(trail.description)
        locationInput.setText(trail.location)
        difficultyInput.setText(trail.difficulty)
        distanceInput.setText(trail.distance.toString())
        ratingInput.rating = trail.rating.toFloat()
    }

    private fun saveTrail() {
        val name = nameInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val location = locationInput.text.toString().trim()
        val difficulty = difficultyInput.text.toString().trim()

        if (!isValidText(name)) {
            nameInput.error = "Enter a name using letters"
            return
        }

        if (!isValidText(description)) {
            descriptionInput.error = "Enter a description using letters"
            return
        }

        if (!isValidText(location)) {
            locationInput.error = "Enter a location using letters"
            return
        }

        if (!isValidText(difficulty)) {
            difficultyInput.error = "Enter a difficulty using letters"
            return
        }

        val distance = distanceInput.text.toString().trim().toDoubleOrNull()
        if (distance == null || distance < 0) {
            distanceInput.error = "Enter a valid number (km)"
            return
        }

        val rating = ratingInput.rating.toDouble()

        if (editingId == null) {
            AppData.trails.create(
                Trail(
                    name = name,
                    description = description,
                    location = location,
                    difficulty = difficulty,
                    distance = distance,
                    rating = rating
                )
            )
            Toast.makeText(this, "Trail created", Toast.LENGTH_SHORT).show()
        } else {
            AppData.trails.update(
                Trail(
                    id = editingId!!,
                    name = name,
                    description = description,
                    location = location,
                    difficulty = difficulty,
                    distance = distance,
                    rating = rating
                )
            )
            Toast.makeText(this, "Trail updated", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    /** Non-empty text that includes at least one letter (rejects numbers-only input). */
    private fun isValidText(value: String): Boolean {
        return value.isNotEmpty() && value.any { it.isLetter() }
    }
}
