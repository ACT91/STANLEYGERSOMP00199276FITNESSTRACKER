package com.example.stanleygersomp00199276fitnesstracker.utils

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewAnimator {

    /**
     * Apply slide-in animation to RecyclerView items
     */
    fun applySlideInAnimation(recyclerView: RecyclerView) {
        val animator = DefaultItemAnimator()
        animator.addDuration = 300
        animator.removeDuration = 300
        recyclerView.itemAnimator = animator
    }

    /**
     * Animate RecyclerView item on bind (using code-based animation instead of XML)
     */
    fun animateItemView(viewHolder: RecyclerView.ViewHolder, position: Int) {
        // Simple alpha animation
        viewHolder.itemView.alpha = 0f
        viewHolder.itemView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    /**
     * Apply fade-in animation to RecyclerView
     */
    fun applyFadeInAnimation(recyclerView: RecyclerView) {
        recyclerView.alpha = 0f
        recyclerView.animate()
            .alpha(1f)
            .setDuration(400)
            .start()
    }
}

