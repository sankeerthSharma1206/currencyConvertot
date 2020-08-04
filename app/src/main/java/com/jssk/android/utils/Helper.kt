package com.jssk.android.utils

import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.jssk.android.R
import java.io.File

object Helper {
    private var toast: Toast? = null
    private var progressDialog: ProgressDialog? = null

    fun showToast(context: Context, msg: String) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun displayProgressBar(context: Context) {
        dismissProgressBar()
        progressDialog = ProgressDialog(context)
        progressDialog?.setMessage(context.getString(R.string.please_wait))
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    fun dismissProgressBar() {
        progressDialog?.dismiss()
    }

    fun isNetworkAvailable(): Boolean {
        var result = false
        val cm =
            JSSKApp.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun showErrorDialog(context: Context, message: String?) {
        try {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_error)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogScaling50
            dialog.setCancelable(false)
            val msg = dialog.findViewById<TextView>(R.id.message)
            msg.text = message
            dialog.findViewById<View>(R.id.ok)
                .setOnClickListener {
                    dialog.dismiss()
                }
            dialog.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun showSuccessDialog(context: Context?, message: String?) {
        try {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.dialog_success)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogScaling50
            dialog.setCancelable(false)
            val msg = dialog.findViewById<TextView>(R.id.message)
            msg.text = message
            dialog.findViewById<View>(R.id.ok).setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getCacheImagePath(context: Context, fileName: String?): Uri? {
        val path = File(context.externalCacheDir, "camera")
        if (!path.exists())
            path.mkdirs()
        val image = File(path, fileName)
        return FileProvider.getUriForFile(context, context.packageName + ".provider", image)
    }

    fun queryName(resolver: ContentResolver, uri: Uri?): String? {
        val returnCursor = resolver.query(uri!!, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    fun getHtmlText(str: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
        else
            Html.fromHtml(str)
    }

    /*fun subscribeToTopics() {
        val messaging = FirebaseMessaging.getInstance()
        try {
            if (PrefManager.getUserStringData(Constants.keyRole).equals(Constants.ROLE_ADMIN))
                messaging.subscribeToTopic(Constants.topicAdmin)
            messaging.subscribeToTopic(PrefManager.getUserStringData(Constants.keyMobile)!!)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun unSubscribeFromTopics() {
        val messaging = FirebaseMessaging.getInstance()
        try {
            if (PrefManager.getUserStringData(Constants.keyRole).equals(Constants.ROLE_ADMIN))
                messaging.unsubscribeFromTopic(Constants.topicAdmin)
            messaging.unsubscribeFromTopic(PrefManager.getUserStringData(Constants.keyMobile)!!)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }*/

    fun clearCache(context: Context) {
        val path = File(context.externalCacheDir, "camera")
        if (path.exists() && path.isDirectory) {
            for (child in path.listFiles()) {
                child.delete()
            }
        }
    }
}