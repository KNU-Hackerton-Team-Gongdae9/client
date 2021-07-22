package com.example.knuhack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_write_post.*

class write_post : AppCompatActivity(),ItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

//        val btn = findViewById<Button>(R.id.slect_board) as Button
//        btn.setOnClickListener {
//            openBottomSheet()
//        }
        slect_board.setOnClickListener { openBottomSheet() }
    }
    fun openBottomSheet(){
        val addPhotoBottomDialogFragment = ActionBottom.newInstance()
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,ActionBottom.TAG
        )
    }
    override fun onItemClick(item: String?) {
        slect_board.text = "$item"
    }
}
