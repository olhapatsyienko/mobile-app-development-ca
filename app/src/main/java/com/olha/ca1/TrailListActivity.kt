package com.olha.ca1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrailListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail_list)

        recyclerView = findViewById(R.id.trailsRecyclerView)

        adapter = TrailAdapter(
            trails = AppData.trails.findAll(),
            onEdit = { trail -> editTrail(trail) },
            onDelete = { trail -> deleteTrail(trail) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.updateTrails(AppData.trails.findAll())
        }
    }

    private fun editTrail(trail: Trail) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", trail.id)
        startActivity(intent)
    }

    private fun deleteTrail(trail: Trail) {
        AppData.trails.delete(trail.id)
        adapter.updateTrails(AppData.trails.findAll())
    }
}