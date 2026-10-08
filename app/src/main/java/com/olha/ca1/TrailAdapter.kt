package com.olha.ca1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrailAdapter(
    private var trails: List<Trail>,
    private val onEdit: (Trail) -> Unit,
    private val onDelete: (Trail) -> Unit
) : RecyclerView.Adapter<TrailAdapter.TrailViewHolder>() {

    class TrailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
        val locationText: TextView = itemView.findViewById(R.id.locationText)
        val detailsText: TextView = itemView.findViewById(R.id.detailsText)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trail, parent, false)
        return TrailViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val trail = trails[position]

        holder.nameText.text = "${trail.id}: ${trail.name}"
        holder.descriptionText.text = trail.description
        holder.locationText.text = trail.location
        holder.detailsText.text =
            "Difficulty: ${trail.difficulty}  |  ${trail.distance} km  |  Rating: ${trail.rating}"

        holder.editButton.setOnClickListener { onEdit(trail) }
        holder.deleteButton.setOnClickListener { onDelete(trail) }
    }

    override fun getItemCount(): Int = trails.size

    fun updateTrails(newTrails: List<Trail>) {
        trails = newTrails
        notifyDataSetChanged()
    }
}