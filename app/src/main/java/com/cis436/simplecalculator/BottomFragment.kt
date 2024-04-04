package com.cis436.simplecalculator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cis436.simplecalculator.databinding.FragmentBottomBinding
import kotlin.ClassCastException

class BottomFragment : Fragment() {

    private lateinit var binding: FragmentBottomBinding
    private lateinit var activityCallback: BottomListener

    interface BottomListener {
        fun onButtonClick(buttonValue: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as BottomListener
        }
        catch(e : ClassCastException) {
            throw ClassCastException(context.toString() +
                    " must implement BottomListener")
        }
    } //end of onAttach

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomBinding.inflate(inflater, container, false)

        // list of number and point buttons
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnPoint
        )

        // list of operation buttons
        val operationButtons = listOf(
            R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnModulus, R.id.btnSquareRoot
        )

        // assign setOnClickListener for all number and point buttons
        numberButtons.forEach { buttonId ->
            binding.root.findViewById<Button>(buttonId).setOnClickListener { button ->
                val buttonValue = (button as? Button)?.tag.toString()
                activityCallback.onButtonClick(buttonValue)
            }
        }

        // assign setOnClickListener for all operation buttons
        operationButtons.forEach { buttonId ->
            binding.root.findViewById<Button>(buttonId).setOnClickListener { button ->
                val buttonValue = (button as? Button)?.tag.toString()
                activityCallback.onButtonClick(buttonValue)
                enableOpButtons(false)
            }

        }

        // assign setOnClickListener for C,CE, and = buttons
        binding.btnC.setOnClickListener { activityCallback.onButtonClick("C") }
        binding.btnCE.setOnClickListener { activityCallback.onButtonClick("CE") }
        binding.btnEqualTo.setOnClickListener { activityCallback.onButtonClick("=") }


        return binding.root
    } //end of onCreateView

    // enable/disable operation buttons
    fun enableOpButtons(enable: Boolean) {
        val operationButtons = listOf(
            R.id.btnMultiply, R.id.btnDivide, R.id.btnModulus, R.id.btnSquareRoot
        )

        operationButtons.forEach { buttonId ->
            binding.root.findViewById<Button>(buttonId).isEnabled = enable
        }
    } //end of enableOpButtons

}