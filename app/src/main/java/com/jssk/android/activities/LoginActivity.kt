package com.jssk.android.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jssk.android.R
import com.jssk.android.dtos.UserDTO
import com.jssk.android.utils.Constants
import com.jssk.android.utils.Helper
import com.jssk.android.utils.PrefManager
import com.squareup.phrase.Phrase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private val context: Context = this
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var countDownTimer: CountDownTimer? = null
    private val timeInterval = 30L
    private var verificationID: String? = null
    private var mobile: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        displayNumberLayout(true)
        setListeners()
    }

    private fun setListeners() {
        get_otp_btn.setOnClickListener {
            if (number_layout.visibility == View.VISIBLE)
                sendOTP()
            else
                verifyOTP()
        }
        resend_otp.setOnClickListener {
            sendOTP()
        }
        edit_number.setOnClickListener {
            displayNumberLayout(false)
        }
    }

    private fun stopTimer() {
        timer.text = ""
        resend_otp.isEnabled = true
        resend_otp.setTextColor(ContextCompat.getColor(context, R.color.black))
        countDownTimer?.cancel()
        countDownTimer = null
    }

    private fun sendOTP() {
        if (!Helper.isNetworkAvailable()) {
            Helper.showToast(context, getString(R.string.no_internet))
            return
        }
        mobile = mobile_number.text.toString().trim { it <= ' ' }
        when {
            mobile!!.isEmpty() -> Helper.showErrorDialog(
                context, getString(R.string.empty_mobile_number)
            )
            mobile!!.length != 10 -> Helper.showErrorDialog(
                context, getString(R.string.incorrect_mobile_number)
            )
            else -> {
                Helper.displayProgressBar(context)
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91$mobile", timeInterval, TimeUnit.SECONDS,
                    this,
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                            if (phoneAuthCredential.smsCode != null)
                                otp_et.setText(phoneAuthCredential.smsCode)
                            Helper.showToast(context, getString(R.string.verification_success))
                            stopTimer()
                            login(phoneAuthCredential)
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            Helper.dismissProgressBar()
                            e.printStackTrace()
                            when (e) {
                                is FirebaseTooManyRequestsException -> Helper.showToast(
                                    context, getString(R.string.too_many_verify_attempts)
                                )
                                is FirebaseAuthException -> Helper.showToast(
                                    context, context.getString(R.string.server_contact_failed)
                                )
                                else -> Helper.showToast(
                                    context, getString(R.string.verification_failed)
                                )
                            }
                        }

                        override fun onCodeSent(
                            s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken
                        ) {
                            super.onCodeSent(s, forceResendingToken)
                            verificationID = s
                            Helper.dismissProgressBar()
                            Helper.showToast(context, getString(R.string.code_sent))
                            displayVerifyLayout()
                            startTimer()
                        }
                    })
            }
        }
    }

    private fun startTimer() {
        timer.text = "20S"
        resend_otp.isEnabled = false
        resend_otp.setTextColor(ContextCompat.getColor(context, R.color.grey))
        countDownTimer = object : CountDownTimer(timeInterval * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.text = "${millisUntilFinished / 1000}S"
            }

            override fun onFinish() {
                stopTimer()
            }
        }.start()
    }

    private fun displayVerifyLayout() {
        number_layout.visibility = View.GONE
        verification_layout.visibility = View.VISIBLE
        val phrase = Phrase.from(getString(R.string.verification_code_sent_template))
            .put("mobile_number", mobile)
            .format()
        label2.text = phrase.toString()
        label3.text = getString(R.string.enter_code_here)
        otp_et.setText("")
        get_otp_btn.text = getString(R.string.verify_proceed)
    }

    private fun displayNumberLayout(clearText: Boolean) {
        number_layout.visibility = View.VISIBLE
        verification_layout.visibility = View.GONE
        if (clearText)
            mobile_number.setText("")
        label2.text = getString(R.string.verification_label)
        label3.text = getString(R.string.enter_code_here)
        get_otp_btn.text = getString(R.string.get_otp)
        verificationID = null
        stopTimer()
    }

    private fun verifyOTP() {
        if (!Helper.isNetworkAvailable()) {
            Helper.showToast(context, getString(R.string.no_internet))
            return
        }
        val valOTP: String = otp_et.text.toString().trim { it <= ' ' }
        if (valOTP.isEmpty()) {
            Helper.showToast(context, getString(R.string.empty_otp))
            return
        }
        if (verificationID == null) {
            Helper.showErrorDialog(context, context.getString(R.string.error_occurred))
            return
        }
        stopTimer()
        Helper.displayProgressBar(context)

        val credential = PhoneAuthProvider.getCredential(
            verificationID!!,
            otp_et.text.toString().trim { it <= ' ' }
        )
        login(credential)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.confirmation))
        builder.setMessage(getString(R.string.exit_confirmation))
        builder.setPositiveButton(
            getString(R.string.yes).toUpperCase(
                Locale.ROOT
            )
        ) { _: DialogInterface?, _: Int -> finish() }
        builder.setNegativeButton(getString(R.string.no).toUpperCase(Locale.ROOT)) { _: DialogInterface?, _: Int -> }
        builder.show()
    }

    private fun login(credential: PhoneAuthCredential) {
        Helper.displayProgressBar(context)
        if (auth.currentUser != null)
            auth.signOut()
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                when {
                    task.isSuccessful -> {
                        firestore.collection(Constants.COLLECTION_USERS)
                            .document(mobile_number.text.toString().trim()).get()
                            .addOnCompleteListener { task1: Task<DocumentSnapshot?> ->
                                Helper.dismissProgressBar()
                                if (task1.isSuccessful) {
                                    val document = task1.result
                                    if (document != null && document.exists()) {
                                        val user: UserDTO? = document.toObject(UserDTO::class.java)
                                        if (user != null) {
                                            if (user.enabled == false)
                                                Helper.showErrorDialog(
                                                    context, getString(R.string.disabled_account)
                                                )
                                            else {
                                                Helper.showToast(
                                                    context, getString(R.string.success)
                                                )
                                                PrefManager.saveUserDTOData(user)
                                                Helper.subscribeToTopics()
                                                startActivity(
                                                    Intent(context, MainActivity::class.java)
                                                )
                                                finish()
                                            }
                                        } else
                                            Helper.showErrorDialog(
                                                context, getString(R.string.failed_to_find_user)
                                            )
                                    } else {
                                        val intent = Intent(context, UserRegistration::class.java)
                                        intent.putExtra("mobile", mobile)
                                        startActivityForResult(intent, 567)
                                    }
                                } else {
                                    Helper.showErrorDialog(
                                        context, getString(R.string.data_fetching_failed)
                                    )
                                    task1.exception?.printStackTrace()
                                }
                            }
                    }
                    task.exception is FirebaseAuthInvalidCredentialsException -> {
                        Helper.dismissProgressBar()
                        Helper.showToast(context, getString(R.string.incorrect_code))
                    }
                    else -> {
                        Helper.dismissProgressBar()
                        Helper.showToast(context, getString(R.string.sign_in_failed))
                    }
                }
            }
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 567 && resultCode == 789) {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        } else if(requestCode == 567 && resultCode == 890)
            displayNumberLayout(true)
    }

}