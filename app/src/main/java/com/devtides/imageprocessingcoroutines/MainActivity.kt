package com.devtides.imageprocessingcoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("Coroutine Error", throwable.localizedMessage, throwable)
        }

        coroutineScope.launch(coroutineExceptionHandler) {
            val originalBitmap = withContext(Dispatchers.IO) {
                return@withContext fetchOriginalBitmap()
            }

            val filteredImage = withContext(Dispatchers.Default) {
                return@withContext Filter.apply(originalBitmap)
            }

            displayImage(filteredImage)

        }


    }

    private fun fetchOriginalBitmap(): Bitmap = URL(IMAGE_URL).openStream().use {
        BitmapFactory.decodeStream(it)
    }

    private fun displayImage(bitmap: Bitmap) {
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bitmap)
        imageView.visibility = View.VISIBLE
    }
}
