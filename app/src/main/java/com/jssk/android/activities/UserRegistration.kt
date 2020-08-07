package com.jssk.android.activities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.jssk.android.R
import com.jssk.android.dtos.UserDTO
import com.jssk.android.utils.Constants
import com.jssk.android.utils.Helper
import com.jssk.android.utils.PrefManager
import kotlinx.android.synthetic.main.activity_user_registration.*

class UserRegistration : AppCompatActivity() {
    private val context: Context = this
    private var mobile: String? = null
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
        mobile = intent?.getStringExtra("mobile")
        number.setText(mobile)
        setListeners()
    }

    private fun setListeners() {
        number.setOnClickListener {
            Helper.showToast(context, getString(R.string.cant_edit_number))
        }
        submit.setOnClickListener {
            val name = name.text.toString().trim()
            val number2 = number2.text.toString().trim()
            val email = email_address.text.toString().trim()
            val address = address.text.toString().trim()
            if (name.isEmpty()) {
                Helper.showErrorDialog(context, getString(R.string.empty_name))
                return@setOnClickListener
            }
            if (address.isEmpty()) {
                Helper.showErrorDialog(context, getString(R.string.empty_address))
                return@setOnClickListener
            }
            if(number2.isNotEmpty() && number2.length != 10) {
                Helper.showErrorDialog(context, getString(R.string.check_alternate_number))
                return@setOnClickListener
            }
            val user = UserDTO(
                Constants.ROLE_USER, name, mobile!!, number2, email, address
            )
            Helper.displayProgressBar(context)
            firestore.collection(Constants.COLLECTION_USERS).document(mobile!!)
                .set(user.getMap()).addOnCompleteListener { task ->
                    Helper.dismissProgressBar()
                    if (task.isSuccessful) {
                        PrefManager.saveUserDTOData(user)
                        Helper.subscribeToTopics()
                        showSuccessDialog()
                    } else
                        Helper.showErrorDialog(context, getString(R.string.registration_failed))
                }
        }
    }

    private fun showSuccessDialog() {
        try {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_success)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogScaling50
            dialog.setCancelable(false)
            val msg = dialog.findViewById<TextView>(R.id.message)
            msg.text = getString(R.string.registration_success)
            dialog.findViewById<View>(R.id.ok).setOnClickListener {
                dialog.dismiss()
                Handler().postDelayed({
                    setResult(789)
                    finish()
                }, 400)
            }
            dialog.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBackPressed() {
        setResult(890)
        super.onBackPressed()
    }
}