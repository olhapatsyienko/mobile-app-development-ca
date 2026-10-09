package com.olha.ca1

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

    val navigationView = findViewById<NavigationView>(R.id.navView)
    for (index in 0 until navigationView.menu.size()) {
        val item = navigationView.menu.getItem(index)
        item.isEnabled = false
        item.isCheckable = false
    }
    navigationView.setNavigationItemSelectedListener { false }
}