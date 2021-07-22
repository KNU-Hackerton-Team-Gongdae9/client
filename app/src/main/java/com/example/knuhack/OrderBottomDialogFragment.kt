package com.example.knuhack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class OrderBottomDialogFragment(context: Context) : BottomSheetDialogFragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.activity_order_bottom_dialog_fragment, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)//button
    {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<Button>(R.id.slect_qna)?.setOnClickListener {
            Toast.makeText(context, "Bottom Sheet 안의 버튼 클릭", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}