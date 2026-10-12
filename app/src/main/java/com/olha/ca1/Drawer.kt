package com.olha.ca1

import android.content.Intent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

fun AppCompatActivity.attachDrawer() {
    val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)

    val toggle = ActionBarDrawerToggle(
        this,
        drawerLayout,
        toolbar,
        R.string.drawer_open,
        R.string.drawer_close
    )
    drawerLayout.addDrawerListener(toggle)
    toggle.syncState()
    drawerLayout.post { toggle.syncState() }

    val content = toolbar.parent as View
    ViewCompat.setOnApplyWindowInsetsListener(content) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets
    }
    ViewCompat.requestApplyInsets(content)

    val navigationView = findViewById<NavigationView>(R.id.navView)
    navigationView.setNavigationItemSelectedListener { item ->
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_home -> openDrawerPage(MainActivity::class.java)
            R.id.nav_trail_list -> openDrawerPage(TrailListActivity::class.java)
            R.id.nav_add_trail -> openAddTrailPage()
        }
        true
    }
}

private fun AppCompatActivity.openDrawerPage(destination: Class<out AppCompatActivity>) {
    if (this::class.java == destination) {
        return
    }
    startActivity(
        Intent(this, destination).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    )
}

private fun AppCompatActivity.openAddTrailPage() {
    val editingExistingTrail = this is AddEditActivity && intent.getLongExtra("id", -1L) != -1L
    if (this is AddEditActivity && !editingExistingTrail) {
        return
    }
    startActivity(
        Intent(this, AddEditActivity::class.java).apply {
            addFlags(
                if (editingExistingTrail) {
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                } else {
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
            )
        }
    )
}