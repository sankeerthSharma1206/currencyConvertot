package com.jssk.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.jssk.android.R
import com.jssk.android.utils.Helper
import com.jssk.android.utils.PrefManager
import kotlinx.android.synthetic.main.frag_profile.*

class Profile: Fragment() {
    private val user = PrefManager.getUserDTO()
    private val reference = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setData()
        setListeners()
    }

    private fun setData() {
        initials.text = Helper.getInitials(user?.name!!)
        user_name.text = user.name!!
        user_mobile.text = user.mobile!!
        name.setText(user.name)
        number.setText(user.mobile)
        number2.setText(user.mobile2)
        email_address.setText(user.email)
        address.setText(user.address)
        reference.child(Helper.getProfilePicPath(user.mobile!!)).downloadUrl
            .addOnCompleteListener {
                Glide.with(context!!)
                    .load(it)
                    .into(avatar)
            }
    }

    private fun setListeners() {
        edit_icon.setOnClickListener {

        }
        submit.setOnClickListener {

        }
    }

}