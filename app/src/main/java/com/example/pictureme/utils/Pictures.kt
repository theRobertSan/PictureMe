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
        // Load default profile picture
        if (profilePicturePath == null) {
            imageCreator.load("https://firebasestorage.googleapis.com/v0/b/pictureme-369323.appspot.com/o/profile_pictures%2Fdefault_profile_picture.jpg?alt=media&token=769ca217-1acb-4298-950a-400311c0187e") {
                crossfade(true)
                crossfade(1000)
                listener { _, _ ->
                    imageLoadingBar.isGone = true
                }
            }
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