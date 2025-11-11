package com.example.stanleygersomp00199276fitnesstracker.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.stanleygersomp00199276fitnesstracker.databinding.ActivityAboutBinding
import com.example.stanleygersomp00199276fitnesstracker.utils.ToolbarUtils

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, com.example.stanleygersomp00199276fitnesstracker.R.color.gunmetal_green)

        setupToolbar()
        loadCreatorImage()

    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        // Ensure toolbar icons and title are white
        ToolbarUtils.tintToolbarIconsWhite(binding.toolbar)
    }



    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadCreatorImage() {
        val fileName = "BackgroundEraser_20250628_183005300.jpg"
        try {
            val assetManager = assets

            // Some projects mistakenly place assets under app/src/assets (missing 'main').
            // Try common asset roots: "" (root of main assets) and "../assets" won't work via AssetManager,
            // so attempt a direct open first and then check assetManager.list.
            var loaded = false

            // 1) Try direct open (if asset in packaged assets root)
            try {
                assetManager.open(fileName).use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.ivCreatorImage.setImageBitmap(bitmap)
                    loaded = true
                }
            } catch (_: Exception) {
                // ignore and try listing
            }

            if (!loaded) {
                val assetList = assetManager.list("")?.toList() ?: emptyList()
                if (assetList.contains(fileName)) {
                    assetManager.open(fileName).use { inputStream ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.ivCreatorImage.setImageBitmap(bitmap)
                        loaded = true
                    }
                }
            }

            if (!loaded) {
                // Try drawable resource named 'creator_image' (res/drawable/creator_image.png)
                val drawableName = "creator_image"
                val resId = resources.getIdentifier(drawableName, "drawable", packageName)
                if (resId != 0) {
                    binding.ivCreatorImage.setImageResource(resId)
                    loaded = true
                }
            }

            if (!loaded) {
                // Final fallback: use existing drawable
                binding.ivCreatorImage.setImageResource(com.example.stanleygersomp00199276fitnesstracker.R.drawable.ic_fitness)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // On any error, show a fallback drawable so the UI doesn't remain empty
            binding.ivCreatorImage.setImageResource(com.example.stanleygersomp00199276fitnesstracker.R.drawable.ic_fitness)
        }
    }
}
