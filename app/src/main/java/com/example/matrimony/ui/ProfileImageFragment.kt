package com.example.matrimony.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.ImageView
import com.example.matrimony.R

class ProfileImageFragment(imageResource: Int) : Fragment() {

    private var imageResource: Int

    init {
        this.imageResource = imageResource
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_image, container, false)
        val imageView = view.findViewById<ImageView>(R.id.iv_profile_pic)
        imageView.setImageResource(imageResource)
        imageView.layoutParams.width=LayoutParams.MATCH_PARENT
        imageView.layoutParams.height=LayoutParams.MATCH_PARENT

        return view
    }
}
