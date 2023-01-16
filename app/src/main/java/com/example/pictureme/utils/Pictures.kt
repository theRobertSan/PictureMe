package com.example.pictureme.utils

import androidx.core.view.isGone
import androidx.core.widget.ContentLoadingProgressBar
import coil.load
import com.example.pictureme.R
import com.google.android.material.imageview.ShapeableImageView

object Pictures {

    fun loadPicme(
        imagePath: String?,
        imagePicme: ShapeableImageView,
        picmeLoadingBar: ContentLoadingProgressBar
    ) {
        imagePicme.load(imagePath) {
            crossfade(true)
            crossfade(1000)
            listener { _, _ ->
                picmeLoadingBar.isGone = true
            }
        }
    }

    fun loadProfilePicture(
        profilePicturePath: String?,
        imageCreator: ShapeableImageView,
        imageLoadingBar: ContentLoadingProgressBar
    ) {
        if (profilePicturePath == null) {
            imageCreator.setImageResource(R.drawable.default_profile_picture)
            imageLoadingBar.isGone = true
        } else {
            imageCreator.load(profilePicturePath) {
                crossfade(true)
                crossfade(1000)
                listener { _, _ ->
                    imageLoadingBar.isGone = true
                }
            }
        }
    }

}