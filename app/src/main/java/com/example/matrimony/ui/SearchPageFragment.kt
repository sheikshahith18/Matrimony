package com.example.matrimony.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.databinding.FragmentSearchPageBinding


class SearchPageFragment : Fragment() {

    lateinit var binding: FragmentSearchPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_search_page,container,false)

        binding.etSearch.setOnFocusChangeListener { editText, hasFocus ->

            if(hasFocus){
                binding.imgBtnBackArrow.visibility=View.VISIBLE
                binding.imgBtnSort.visibility=View.GONE
            }else{
                binding.imgBtnBackArrow.visibility=View.GONE
                binding.imgBtnSort.visibility=View.VISIBLE
            }
        }

        binding.imgBtnBackArrow.setOnClickListener {
            binding.etSearch.apply {
                clearFocus()
                setText("")
            }

        }

        return binding.root
    }

}