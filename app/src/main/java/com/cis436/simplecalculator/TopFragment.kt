package com.cis436.simplecalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cis436.simplecalculator.databinding.FragmentTopBinding

class TopFragment : Fragment() {

    private lateinit var binding : FragmentTopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopBinding.inflate(inflater, container, false)
        return binding.root
    } //end of onCreateView

    // change text in the text view (when getting calculation result)
    fun changeTextDisplay(text: String) {
        binding.textViewNumbers.text = text
    }

    // append text in the text view (when clicking buttons)
    fun appendTextDisplay(text: String) {
        binding.textViewNumbers.append(text)
    }

    // function for CE button (clear recent entry in the text view)
    fun clearRecentEntry() {
        if (binding.textViewNumbers.text.isNotEmpty()) {
            binding.textViewNumbers.text = binding.textViewNumbers.text.dropLast(1)
        }
    }

}

