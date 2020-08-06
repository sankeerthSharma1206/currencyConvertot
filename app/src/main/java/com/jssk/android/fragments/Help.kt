package com.jssk.android.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jssk.android.R
import com.jssk.android.utils.Constants
import kotlinx.android.synthetic.main.frag_help.*

class Help: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spannableString = SpannableString(getString(R.string.help_label))
        val termsStart = spannableString.indexOf("Terms")
        val termsEnd = termsStart + "Terms Of Service".length
        val privacyStart = spannableString.indexOf("Privacy")
        val privacyEnd = privacyStart + "Privacy Policy".length
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TERMS_URL)))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = Color.BLACK
            }
        }
        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_URL)))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = Color.BLACK
            }
        }
        spannableString.setSpan(
            clickableSpan1, termsStart, termsEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), termsStart, termsEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan2, privacyStart, privacyEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), privacyStart, privacyEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        label4.text = spannableString
        label4.movementMethod = LinkMovementMethod.getInstance()
    }
}