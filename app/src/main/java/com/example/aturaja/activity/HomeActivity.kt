package com.example.aturaja.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.Adapter
import com.example.aturaja.data.DataSchedule
import com.example.aturaja.data.DataSource
import com.example.aturaja.model.GetAllScheduleResponse2
import com.example.aturaja.model.GetAllTaskResponse2
import com.example.aturaja.model.GetScheduleResponse
import com.example.aturaja.model.GetTaskResponse
import com.example.aturaja.network.APIClient
import com.example.aturaja.session.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fabAddSchedule: FloatingActionButton
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var textScheduleTotal: TextView
    private lateinit var textSizeTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_2)

        val myDataset = DataSource().loadSchedule()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        navigationView = findViewById(R.id.nav_view)

        recyclerView.adapter = Adapter(this, myDataset)

        recyclerView.setHasFixedSize(true)
        iniComponent()
        scheduleSize()
        taskSize()


        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        navigationView.setNavigationItemSelectedListener{item ->
            when(item.itemId) {
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
                    startActivity(Intent(this, FriendListActivity::class.java))
                    true
                }
                R.id.tim -> {
                    Toast.makeText(this, "buka tim", Toast.LENGTH_LONG).show()

                    true
                }
                R.id.mode_fokus -> {
                    startActivity(Intent(applicationContext, FocusActivity::class.java))
                    true
                }
                else -> false
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.page_2 -> {
                    startActivity(Intent (applicationContext, CalendarActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_3 -> {
                    Toast.makeText(this, "buka schedule", Toast.LENGTH_LONG).show()
                    startActivity(Intent (applicationContext, AddTodo::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_5 -> {
                    startActivity(Intent(applicationContext, TaskActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        fabAddSchedule.setOnClickListener {
            val myIntent = Intent(applicationContext, AddScheduleActivity::class.java)

            startActivity(myIntent)
            finish()
        }

        fabAddTask.setOnClickListener {
            startActivity(Intent(applicationContext, AddTaskActivity::class.java))
        }
    }

    fun iniComponent() {
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.topAppBar)
        navigationView = findViewById(R.id.nav_view)
        fabAddSchedule = findViewById(R.id.floating_add_schedule_button)
        fabAddTask = findViewById(R.id.floating_add_task_button)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        textScheduleTotal = findViewById(R.id.scheduelSize)
        textSizeTotal = findViewById(R.id.taskSize)
    }

    fun taskSize() {
        var apiClient = APIClient()

        apiClient.getApiService(this).getTask()
            .enqueue(object: Callback<GetAllTaskResponse2>{
                override fun onResponse(
                    call: Call<GetAllTaskResponse2>,
                    response: Response<GetAllTaskResponse2>
                ) {
                    val response = response.body()

                    if (response != null) {
                        if(response.tasks != null) {
                            textSizeTotal.text = response.tasks.size.toString()
                        } else {
                            textSizeTotal.text = "0"
                        }
                    }
                }

                override fun onFailure(call: Call<GetAllTaskResponse2>, t: Throwable) {
                    Log.d("error size schedule", "$t")
                }
            })
    }

    fun scheduleSize() {
        var apiClient = APIClient()

        apiClient.getApiService(this).getSchedules()
            .enqueue(object: Callback<GetAllScheduleResponse2> {
                override fun onResponse(
                    call: Call<GetAllScheduleResponse2>,
                    response: Response<GetAllScheduleResponse2>
                ) {
                    val response = response.body()

                    if (response != null) {
                        if(response.schedules !== null) {
                            textScheduleTotal.text = response.schedules.size.toString()
                        } else {
                            textScheduleTotal.text = "0"
                        }
                    }
                }

                override fun onFailure(call: Call<GetAllScheduleResponse2>, t: Throwable) {
                    Log.d("error size task", "$t")
                }
            })
    }

    fun signOutOnClick(view: android.view.View) {
        val myIntent = Intent(applicationContext, LoginActivity::class.java)

        SessionManager(this).clearTokenAndUsername()
        startActivity(myIntent)
        finish()
    }
}
