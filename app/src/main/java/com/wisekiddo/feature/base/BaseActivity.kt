package com.wisekiddo.feature.base

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.wisekiddo.application.base.BaseView
import com.wisekiddo.feature.callbacks.AlertCallBack
import com.wisekiddo.presentation.MainIntent
import com.wisekiddo.presentation.MainUIModel

/**
 * Base Activity for all
 * Handles dependency injection initiation
 * Provides dependency to sub classes
 * Handles toast messages
 * Handles alert messages
 * Provides methods for UI handling
 */
 abstract class BaseActivity : AppCompatActivity(), AlertCallBack,  BaseView<MainIntent, MainUIModel>  {

     private lateinit var alertDialog: AlertDialog

     fun showToast(msg: String) {
         runOnUiThread {
             val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
             toast.setGravity(Gravity.BOTTOM, 0, 0)
             toast.show()
         }
     }

    fun showSnackbar(msg:String){
        Snackbar.make(window.decorView.rootView, msg, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

     fun showAlert(message :String,positiveText: Int, negativeText:Int){
         val dialogBuilder = AlertDialog.Builder(this)
         dialogBuilder.setMessage(message)
         dialogBuilder.setPositiveButton(positiveText) { _, _ -> positiveAlertCallBack() }
         dialogBuilder.setNegativeButton(negativeText) { _, _ -> negativeAlertCallBack() }
         alertDialog = dialogBuilder.create()
         alertDialog.setCancelable(false)
         alertDialog.show()
     }

     abstract fun renderView()
     abstract fun initialize()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderView()
        initialize()
    }

     override fun negativeAlertCallBack() {
         alertDialog.dismiss()
     }

     override fun positiveAlertCallBack() {
         alertDialog.dismiss()
     }

}

