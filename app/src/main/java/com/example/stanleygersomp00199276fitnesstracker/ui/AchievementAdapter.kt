package com.example.stanleygersomp00199276fitnesstracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stanleygersomp00199276fitnesstracker.R
import com.example.stanleygersomp00199276fitnesstracker.models.Achievement

class AchievementAdapter(private val items: List<Achievement>) : RecyclerView.Adapter<AchievementAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivAchievementIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvAchievementTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvAchievementDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_achievement, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = items[position]
        holder.tvTitle.text = it.title
        holder.tvDesc.text = it.description
        holder.ivIcon.alpha = if (it.unlocked) 1.0f else 0.3f
    }

    override fun getItemCount(): Int = items.size
}
