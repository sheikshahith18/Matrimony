package com.example.matrimony.ui

import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.matrimony.R

class ProfilePicFragment(val image:Bitmap) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_pic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView=view.findViewById<ImageView>(R.id.image)
        Glide.with(requireActivity())
            .load(image)
            .into(imageView)
    }

}