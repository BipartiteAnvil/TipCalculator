package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.constraintlayout.widget.StateSet.TAG
import androidx.core.content.ContextCompat
import com.example.tipcalculator.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sbTip.progress = INITIAL_TIP_PERCENT
        binding.tvTipParcent.text = "${INITIAL_TIP_PERCENT}%"
        updateTipDescription(INITIAL_TIP_PERCENT)

        binding.sbTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, P1: Int, P2: Boolean) {
                Log.i(TAG, "On progress Changed $P1")
                binding.tvTipParcent.text = "$P1"
                computeTipAndTotal()
                updateTipDescription(P1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        binding.etBaseAmount.addTextChangedListener(object: TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
                }
            })
    }

    private fun updateTipDescription(tipPercent: Int) {

        val tipDescriprtion = when(tipPercent){
            in 0..9 -> "Poor !"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great !"
            else -> "Amazing !!!"

        }
        binding.tvTipDescription.text = tipDescriprtion
        //below changing color using interpolation concept
        // best tip -> green ; worst tip -> red
        // tip in between will have color in between
        // android has ARGB-Evaluator to do this
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / binding.sbTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        )   as Int
        binding.tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {

        if(binding.etBaseAmount.text.isEmpty()){
            binding.tvTipAmount.text = ""
            binding.tvTotalAmount.text = ""
            return
        }

        val baseAmount = binding.etBaseAmount.text.toString().toDouble()
        val tipPercent = binding.sbTip.progress

        val tipAmount = baseAmount * tipPercent/100
        val totalAmount = baseAmount + tipAmount

        binding.tvTipAmount.text = "%.2f".format(tipAmount)  // formatting the sting to show only two digits after decimal
        binding.tvTotalAmount.text = "%.2f".format(tipAmount)

    }
}