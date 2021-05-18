package com.sti.ppam.momadu.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sti.ppam.momadu.R
import kotlinx.android.synthetic.main.fragment_finance.*

class FinanceFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finance, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonExpense.setOnClickListener {
            val intent = Intent(this.context, FinanceExpense::class.java)
            startActivity(intent)
        }
        buttonIncome.setOnClickListener {
            val intent = Intent(this.context, FinanceIncome::class.java)
            startActivity(intent)
        }
    }

    fun addExpense(view: View) {
    }
}