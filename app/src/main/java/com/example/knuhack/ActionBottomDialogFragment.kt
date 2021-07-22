package com.example.knuhack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_order_bottom_dialog_fragment.*
import java.lang.RuntimeException

class ActionBottomDialogFragment :BottomSheetDialogFragment(), View.OnClickListener{

    private var mListener:ItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_order_bottom_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**now call all textView*/
        slect_qna.setOnClickListener(this)
        slect_free.setOnClickListener(this)
        slect_team.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mListener = if (context is ItemClickListener){
            context
        }else{
            throw RuntimeException(
                context.toString() + "Must implement ItemClickListener"
            )
        }

    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(v: View?) {
        val tvSelected = v as TextView
        mListener!!.onItemClick(tvSelected.text.toString())
        dismiss()
    }

}