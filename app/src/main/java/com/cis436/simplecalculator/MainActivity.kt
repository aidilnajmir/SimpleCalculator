package com.cis436.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cis436.simplecalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity(), BottomFragment.BottomListener {

    private var bottomFragment: BottomFragment? = null
    private lateinit var binding: ActivityMainBinding
    private var currentInput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup fragments
        setUpFragments()
    }

    // setup fragments function declaration
    private fun setUpFragments() {
        // check if fragments exist
        val existTopFragment = supportFragmentManager.findFragmentById(R.id.topFragment)
        val existBottomFragment = supportFragmentManager.findFragmentById(R.id.bottomFragment)

        if (existBottomFragment == null) {
            bottomFragment = BottomFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.bottomFragment, bottomFragment!!)
                .commit()
        } else {
            bottomFragment = existBottomFragment as BottomFragment
        }

        if (existTopFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.topFragment, TopFragment())
                .commit()
        }
    } //end of setupFragments

    // onButtonClick function declaration
    override fun onButtonClick(buttonValue: String) {
        val topFragment = supportFragmentManager.findFragmentById(R.id.topFragment) as? TopFragment

        // clear the text view when error message is displayed
        if (listOf("Cannot divide by zero", "Enter a number", "Error").contains(currentInput)) {
            currentInput = ""
            topFragment?.changeTextDisplay(currentInput)
        }

        // handle button clicks
        when (buttonValue) {
            "C" -> {
                currentInput = ""
                topFragment?.changeTextDisplay(currentInput)
                bottomFragment?.enableOpButtons(true)
            }
            "CE" -> {
                // handle clearing square root and recent entry
                if (currentInput.isNotEmpty()) {
                    if (currentInput.endsWith("sqrt(")) {
                        currentInput = currentInput.removeSuffix("sqrt(")
                    } else {
                        currentInput = currentInput.dropLast(1)
                        topFragment?.clearRecentEntry()
                    }
                    topFragment?.changeTextDisplay(currentInput)

                    // enable operation buttons when there is no operation in the current input
                    val containsOperation = currentInput.contains(Regex("sqrt|\\+|-|\\*|/|%"))
                    bottomFragment?.enableOpButtons(!containsOperation)
                }
            }
            "=" -> {
                // handle square root operation (adding ")" before calculation)
                val squareRoot = currentInput.lastIndexOf("sqrt(")
                val closeParenthesis = currentInput.lastIndexOf(")")
                if (squareRoot > closeParenthesis) {
                    currentInput += ")"
                }

                // calculate and display result
                val result = calculation(currentInput)
                currentInput = result
                topFragment?.changeTextDisplay(result)
                bottomFragment?.enableOpButtons(true)
            }
            "√" -> {
                // append square root function
                bottomFragment?.enableOpButtons(false)
                currentInput += "sqrt("
                topFragment?.appendTextDisplay(buttonValue)
            }
            "×" -> {
                // append multiplication operator
                bottomFragment?.enableOpButtons(false)
                currentInput += "*"
                topFragment?.appendTextDisplay(buttonValue)
            }
            "÷" -> {
                // append division operator
                bottomFragment?.enableOpButtons(false)
                currentInput += "/"
                topFragment?.appendTextDisplay(buttonValue)
            }
            else -> {
                // append numbers, operators, or point
                currentInput += buttonValue
                topFragment?.appendTextDisplay(buttonValue)
            }
        }

    } //end of onButtonClick

    // calculation function declaration
    private fun calculation(input: String): String {
        return try {
            val expressionBuilder = ExpressionBuilder(input)
            val expression = expressionBuilder.build()
            val result = expression.evaluate()

            // remove decimal places if result is a whole number
            if (result % 1.0 == 0.0) {
                result.toLong().toString()
            }
            // return result in four decimal places
            else {
                String.format("%.4f", result)
            }
        } catch (e: ArithmeticException) {
            "Cannot divide by zero"
        } catch (e: IllegalArgumentException) {
            "Enter a number"
        } catch (e: Exception) {
            "Error"
        }
    } //end of calculation

}