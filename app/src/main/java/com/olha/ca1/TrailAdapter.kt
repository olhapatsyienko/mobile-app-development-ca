package com.olha.ca1

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

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
        val moreButton: MaterialButton = itemView.findViewById(R.id.moreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trail, parent, false)
        return TrailViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val trail = trails[position]
        val context = holder.itemView.context

        holder.nameText.text = "${trail.id}: ${trail.name}"
        holder.descriptionText.text = trail.description
        holder.locationText.text = trail.location
        holder.detailsText.text =
            "Difficulty: ${trail.difficulty}  |  ${trail.distance} km  |  Rating: ${trail.rating}"

        holder.moreButton.setOnClickListener { view ->
            val popupContext = ContextThemeWrapper(context, R.style.ThemeOverlay_HikingHub_PopupMenu)
            PopupMenu(popupContext, view).apply {
                menuInflater.inflate(R.menu.trail_item_menu, menu)
                setForceShowIcon(true)

                val forest = ContextCompat.getColor(context, R.color.forest)
                val orange = ContextCompat.getColor(context, R.color.trail_orange)

                menu.findItem(R.id.action_edit).apply {
                    title = coloredTitle("Edit", forest)
                    icon = tintedIcon(
                        ContextCompat.getDrawable(context, android.R.drawable.ic_menu_edit),
                        forest
                    )
                }
                menu.findItem(R.id.action_delete).apply {
                    title = coloredTitle("Delete", orange)
                    icon = tintedIcon(
                        ContextCompat.getDrawable(context, android.R.drawable.ic_menu_delete),
                        orange
                    )
                }

                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            onEdit(trail)
                            true
                        }
                        R.id.action_delete -> {
                            onDelete(trail)
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

    private fun coloredTitle(text: String, color: Int): CharSequence {
        return SpannableString(text).apply {
            setSpan(ForegroundColorSpan(color), 0, length, 0)
        }
    }

    private fun tintedIcon(drawable: Drawable?, color: Int): Drawable? {
        return drawable?.mutate()?.let {
            DrawableCompat.wrap(it).also { wrapped ->
                DrawableCompat.setTint(wrapped, color)
            }
        }
    }

    override fun getItemCount(): Int = trails.size

    fun updateTrails(newTrails: List<Trail>) {
        trails = newTrails
        notifyDataSetChanged()
    }
}
