package com.olha.ca1

import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var difficultyInput: Spinner
    private lateinit var distanceInput: EditText
    private lateinit var ratingInput: RatingBar

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit)
        attachDrawer()

        nameInput = findViewById(R.id.nameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        locationInput = findViewById(R.id.locationInput)
        difficultyInput = findViewById(R.id.difficultyInput)
        distanceInput = findViewById(R.id.distanceInput)
        ratingInput = findViewById(R.id.ratingInput)

        // ensure text fields accept letters 
        nameInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        descriptionInput.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        locationInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        distanceInput.inputType = InputType.TYPE_CLASS_TEXT

        val difficulties = resources.getStringArray(R.array.difficulty_levels)
        difficultyInput.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            difficulties
        )

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
        val difficultyIndex = (difficultyInput.adapter as ArrayAdapter<String>)
            .getPosition(trail.difficulty)
        if (difficultyIndex >= 0) {
            difficultyInput.setSelection(difficultyIndex)
        }
        distanceInput.setText(trail.distance.toString())
        ratingInput.rating = trail.rating.toFloat()
    }

    private fun saveTrail() {
        val name = nameInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val location = locationInput.text.toString().trim()
        val difficulty = difficultyInput.selectedItem.toString()

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

    // non empty text that includes at least one letter  
    private fun isValidText(value: String): Boolean {
        return value.isNotEmpty() && value.any { it.isLetter() }
    }
}
