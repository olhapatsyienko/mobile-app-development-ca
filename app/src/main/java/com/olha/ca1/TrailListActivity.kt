package com.olha.ca1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrailListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrailAdapter
    private lateinit var difficultyFilter: Spinner
    private lateinit var locationFilter: Spinner
    private lateinit var sortSpinner: Spinner
    private lateinit var filtersToggle: TextView
    private lateinit var filtersPanel: View

    private var filtersExpanded = false
    private var suppressSpinnerCallback = false

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (!suppressSpinnerCallback) {
                refreshList()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trail_list)
        attachDrawer()

        recyclerView = findViewById(R.id.trailsRecyclerView)
        difficultyFilter = findViewById(R.id.difficultyFilter)
        locationFilter = findViewById(R.id.locationFilter)
        sortSpinner = findViewById(R.id.sortSpinner)
        filtersToggle = findViewById(R.id.filtersToggle)
        filtersPanel = findViewById(R.id.filtersPanel)

        adapter = TrailAdapter(
            trails = emptyList(),
            onEdit = { trail -> editTrail(trail) },
            onDelete = { trail -> deleteTrail(trail) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        setupSpinners()
        setupFiltersToggle()

        findViewById<Button>(R.id.addTrailButton).setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            setupLocationFilterOptions()
            refreshList()
        }
    }

    private fun setupFiltersToggle() {
        filtersPanel.visibility = View.GONE
        filtersToggle.setOnClickListener {
            filtersExpanded = !filtersExpanded
            filtersPanel.visibility = if (filtersExpanded) View.VISIBLE else View.GONE
            updateFiltersToggleLabel()
        }
        updateFiltersToggleLabel()
    }

    private fun updateFiltersToggleLabel() {
        val activeCount = countActiveFilters()
        val label = if (activeCount > 0) {
            getString(R.string.filters_active, activeCount)
        } else {
            getString(R.string.filters)
        }
        val arrow = if (filtersExpanded) " ▲" else " ▼"
        filtersToggle.text = label + arrow
        filtersToggle.contentDescription = if (filtersExpanded) {
            getString(R.string.filters_collapse)
        } else {
            getString(R.string.filters_expand)
        }
    }

    private fun countActiveFilters(): Int {
        val allLabel = getString(R.string.filter_all)
        val defaultSort = resources.getStringArray(R.array.sort_options).first()
        var count = 0
        if (difficultyFilter.selectedItem?.toString() != null &&
            difficultyFilter.selectedItem.toString() != allLabel
        ) {
            count++
        }
        if (locationFilter.selectedItem?.toString() != null &&
            locationFilter.selectedItem.toString() != allLabel
        ) {
            count++
        }
        if (sortSpinner.selectedItem?.toString() != null &&
            sortSpinner.selectedItem.toString() != defaultSort
        ) {
            count++
        }
        return count
    }

    private fun setupSpinners() {
        val allLabel = getString(R.string.filter_all)
        val difficulties = listOf(allLabel) + resources.getStringArray(R.array.difficulty_levels).toList()
        difficultyFilter.adapter = createFilterAdapter(difficulties)

        sortSpinner.adapter = createFilterAdapter(
            resources.getStringArray(R.array.sort_options).toList()
        )

        setupLocationFilterOptions()

        difficultyFilter.onItemSelectedListener = spinnerListener
        locationFilter.onItemSelectedListener = spinnerListener
        sortSpinner.onItemSelectedListener = spinnerListener
    }

    private fun setupLocationFilterOptions() {
        val allLabel = getString(R.string.filter_all)
        val previous = locationFilter.selectedItem?.toString()
        val locations = listOf(allLabel) + AppData.trails.findAll()
            .map { it.location }
            .distinct()
            .sorted()

        suppressSpinnerCallback = true
        locationFilter.adapter = createFilterAdapter(locations)
        val index = locations.indexOf(previous).takeIf { it >= 0 } ?: 0
        locationFilter.setSelection(index)
        suppressSpinnerCallback = false
    }

    private fun createFilterAdapter(items: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(this, R.layout.spinner_item, items).also {
            it.setDropDownViewResource(R.layout.spinner_item)
        }
    }

    private fun refreshList() {
        val allLabel = getString(R.string.filter_all)
        val selectedDifficulty = difficultyFilter.selectedItem?.toString() ?: allLabel
        val selectedLocation = locationFilter.selectedItem?.toString() ?: allLabel
        val selectedSort = sortSpinner.selectedItem?.toString()
            ?: resources.getStringArray(R.array.sort_options).first()

        var trails = AppData.trails.findAll().toList()

        if (selectedDifficulty != allLabel) {
            trails = trails.filter { it.difficulty == selectedDifficulty }
        }
        if (selectedLocation != allLabel) {
            trails = trails.filter { it.location == selectedLocation }
        }

        trails = when (selectedSort) {
            "Distance (shortest)" -> trails.sortedBy { it.distance }
            "Distance (longest)" -> trails.sortedByDescending { it.distance }
            "Rating (highest)" -> trails.sortedByDescending { it.rating }
            "Rating (lowest)" -> trails.sortedBy { it.rating }
            else -> trails.sortedBy { it.id }
        }

        adapter.updateTrails(trails)
        if (::filtersToggle.isInitialized) {
            updateFiltersToggleLabel()
        }
    }

    private fun editTrail(trail: Trail) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", trail.id)
        startActivity(intent)
    }

    private fun deleteTrail(trail: Trail) {
        AppData.trails.delete(trail.id)
        setupLocationFilterOptions()
        refreshList()
    }
}