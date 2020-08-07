package com.jssk.android.activities

import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.jssk.android.R
import com.jssk.android.fragments.Help
import com.jssk.android.fragments.Home
import com.jssk.android.fragments.Profile
import com.jssk.android.utils.Constants
import com.jssk.android.utils.Helper
import com.jssk.android.utils.PrefManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val context: Context = this
    private var fragment: Fragment? = null
    private val auth = FirebaseAuth.getInstance()
    private val reference = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toggle =
            ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        setNavigationHeader()
        setNavigationMenu()
        displaySelectedFragment(R.id.nav_home)
    }

    private fun setNavigationHeader() {
        val header = nav_view.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.name)
        val initials = header.findViewById<TextView>(R.id.initials)
        val mobile = header.findViewById<TextView>(R.id.mobile)
        val profilePic = header.findViewById<ImageView>(R.id.avatar)
        val user = PrefManager.getUserDTO()
        initials.text = Helper.getInitials(user?.name!!)
        mobile.text = user.mobile!!
        name.text = user.name!!
        reference.child(Helper.getProfilePicPath(user.mobile!!)).downloadUrl
            .addOnCompleteListener {
                Glide.with(context)
                    .load(it)
                    .into(profilePic)
            }
    }

    private fun setNavigationMenu() {

    }

    private fun displaySelectedFragment(id: Int) {
        setAppBarElevation(8f)
        when (id) {
            R.id.nav_home -> {
                fragment = Home()
                title = getString(R.string.home)
            }
            R.id.nav_profile -> {
                fragment = Profile()
                title = getString(R.string.profile)
            }
            R.id.nav_help -> {
                title = getString(R.string.help)
                fragment = Help()
            }
            R.id.nav_share -> shareApplication()
            R.id.nav_logout -> logout()
        }
        if (fragment != null) {
            Handler().postDelayed({
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.container, fragment!!)
                ft.commitAllowingStateLoss()
            }, 500)
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        displaySelectedFragment(p0.itemId)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.confirmation))
            builder.setMessage(getString(R.string.want_to_exit))
            builder.setPositiveButton(
                getString(R.string.yes)
            ) { _: DialogInterface?, _: Int -> finish() }
            builder.setNegativeButton(
                getString(R.string.no)
            ) { _: DialogInterface?, _: Int -> }
            builder.show()
        }
    }

    private fun shareApplication() {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            var sAux = "Hey there,\nLet me recommend you this application\n\n"
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + packageName
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            startActivity(Intent.createChooser(i, "choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.confirmation))
        builder.setMessage(getString(R.string.want_to_logout))
        builder.setPositiveButton(R.string.yes) { _: DialogInterface?, _: Int ->
            Helper.unSubscribeFromTopics()
            PrefManager.clearUserPreferences()
            auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            finish()
        }
        builder.setNegativeButton(R.string.no) { _: DialogInterface?, _: Int -> }
        builder.show()
    }

    private fun setAppBarElevation(elevation: Float) {
        app_bar_layout.elevation = elevation
        val stateListAnimator = StateListAnimator()
        stateListAnimator.addState(
            IntArray(0),
            ObjectAnimator.ofFloat(app_bar_layout, "elevation", elevation)
        )
        app_bar_layout.stateListAnimator = stateListAnimator
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}