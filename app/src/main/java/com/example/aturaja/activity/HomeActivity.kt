package com.example.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.Adapter
import com.example.aturaja.data.DataSource
import com.example.aturaja.session.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

//class HomeActivity : AppCompatActivity() {
//    private lateinit var mToggle : ActionBarDrawerToggle
//    private lateinit var drawer_layout: DrawerLayout
//    private lateinit var topAppBar: Toolbar
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.app_bar)
//
//        drawer_layout = findViewById(R.id.drawer_layout)
//        topAppBar = findViewById(R.id.topAppBar)
//        setSupportActionBar(topAppBar)
//
//        topAppBar.setNavigationOnClickListener {
//            drawer_layout.openDrawer(GravityCompat.START)
//        }
//    }
//
//    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
//        return mToggle.onOptionsItemSelected(item)
//    }
//
//
//}

class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fabAddSchedule: FloatingActionButton
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val myDataset = DataSource().loadSchedule()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        navigationView = findViewById(R.id.nav_view)

        recyclerView.adapter = Adapter(this, myDataset)

        recyclerView.setHasFixedSize(true)

        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.topAppBar)
        navigationView = findViewById(R.id.nav_view)
        fabAddSchedule = findViewById(R.id.floating_add_schedule_button)
        fabAddTask = findViewById(R.id.floating_add_task_button)
        bottomNavigation = findViewById(R.id.bottom_navigation)

        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        navigationView.setNavigationItemSelectedListener{item ->
            when(item.itemId) {
                R.id.logout -> {
                    val myIntent = Intent(applicationContext, LoginActivity::class.java)

                    SessionManager.getInstance(applicationContext).clearToken()
                    startActivity(myIntent)

                    true
                }
                R.id.profil -> {
                    Toast.makeText(this, "buka profil", Toast.LENGTH_LONG).show()

                    true
                }
                R.id.setelan -> {
                    Toast.makeText(this, "buka setting", Toast.LENGTH_LONG).show()

                    true
                }
                R.id.teman -> {
                    Toast.makeText(this, "buka teman", Toast.LENGTH_LONG).show()

                    true
                }
                R.id.tim -> {
                    Toast.makeText(this, "buka tim", Toast.LENGTH_LONG).show()

                    true
                }
                else -> false
            }
        }

        fabAddSchedule.setOnClickListener {
            val myIntent = Intent(applicationContext, AddScheduleActivity::class.java)
            startActivity(myIntent)
        }

        fabAddTask.setOnClickListener {
            Toast.makeText(this, "ke hal addNewTask", Toast.LENGTH_LONG).show()
        }
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.logout -> {
//                val myIntent = Intent(applicationContext, LoginActivity::class.java)
//
//                SessionManager.getInstance(applicationContext).clearToken()
//                startActivity(myIntent)
//            }
//            R.id.profil -> {
//                Toast.makeText(this, "buka profil", Toast.LENGTH_LONG).show()
//            }
//            R.id.setelan -> {
//                Toast.makeText(this, "buka setting", Toast.LENGTH_LONG).show()
//            }
//            R.id.teman -> {
//                Toast.makeText(this, "buka teman", Toast.LENGTH_LONG).show()
//            }
//            R.id.tim -> {
//                Toast.makeText(this, "buka tim", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        return true
//    }


//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.profil -> {
//                var myIntent = Intent(applicationContext, LoginActivity::class.java)
//                startActivity(myIntent)
//            }
//        }
//
//        return true
//    }
}