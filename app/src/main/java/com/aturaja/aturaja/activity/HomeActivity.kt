package com.aturaja.aturaja.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.Adapter
import com.aturaja.aturaja.model.*
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.session.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var fabAddSchedule: FloatingActionButton
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var textScheduleTotal: TextView
    private lateinit var textSizeTotal: TextView
    private lateinit var viewAll: TextView
    private lateinit var recyclerView: RecyclerView
    private var arraySorting = ArrayList<SchedulesItem>()
    private var listSchedule = ArrayList<SchedulesItem>()
    private val dateFormatDb = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private var TAG = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_2)

        recyclerView = findViewById(R.id.recycler_view)
        navigationView = findViewById(R.id.nav_view)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        iniComponent()
        scheduleSize()
        taskSize()

        val status  = SessionManager(this).fetchStatusService()

        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        navigationView.setNavigationItemSelectedListener{item ->
            when(item.itemId) {
                R.id.teman -> {
                    startActivity(Intent(this, FriendListActivity::class.java))
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
                    startActivity(Intent (applicationContext, ListScheduleActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_5 -> {
                    startActivity(Intent(applicationContext, ListTaskActivity::class.java))
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

        viewAll.setOnClickListener {
            startActivity(Intent(this, ListScheduleActivity::class.java))
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
        viewAll = findViewById(R.id.textView4)
    }

    fun showRecyclist(date: Date?) {
        var dateFormat: Date
        arraySorting.clear()

        for(i in listSchedule) {
            dateFormat = dateFormatDb.parse(i.schedule?.date)

            if(date == dateFormat) {
                arraySorting.add(i)
                Log.d(TAG, "hasil sorting : $arraySorting")
            }
        }

        if(arraySorting.isNotEmpty()) {
            recyclerView.adapter = Adapter(this, arraySorting)
            textScheduleTotal.text = arraySorting.size.toString()
        } else {
            textScheduleTotal.text = "0"
        }
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
        val cal = Calendar.getInstance()
        val dateNow = dateFormatDb.format(cal.time)
        val date = dateFormatDb.parse(dateNow)

        apiClient.getApiService(this).getSchedules()
            .enqueue(object: Callback<GetAllScheduleResponse2> {
                override fun onResponse(
                    call: Call<GetAllScheduleResponse2>,
                    response: Response<GetAllScheduleResponse2>
                ) {
                    val response2= response.body()

                    if(response.code().equals(200)) {
                        if (response2 != null) {
                            if(response2.schedules !== null) {
                                listSchedule.addAll(response2.schedules)
                                showRecyclist(date)
                            } else {
                                textScheduleTotal.text = "0"
                            }
                        }
                    } else {
                        refreshToken()
                    }
                }

                override fun onFailure(call: Call<GetAllScheduleResponse2>, t: Throwable) {
                    Log.d("error size task", "$t")
                }
            })
    }

    private fun refreshToken() {
        val apiCLient = APIClient()
        val header = "bearer ${SessionManager(this).fetchAuthToken()}"

        apiCLient.getApiService(this).refreshToken(header)
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.code() == 200) {
                        response.body()?.let { SessionManager(applicationContext).saveAuthToken(it.accessToken) }
                        response.body()?.let { SessionManager(applicationContext).saveUsername(it.username) }
                        scheduleSize()
                        taskSize()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
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
