package com.example.aturaja.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.AutoCompleteFriendAdapter
import com.example.aturaja.adapter.AutoCompleteRecomAdapter
import com.example.aturaja.adapter.FriendsAdapterSchedule
import com.example.aturaja.model.*
import com.example.aturaja.network.APIClient
import com.example.aturaja.service.AlarmBroadcast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditDeleteSchedule : AppCompatActivity() {
    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText
    lateinit var editTextPeople: AutoCompleteTextView
    private lateinit var autoCompleteRecom: AutoCompleteTextView
    lateinit var editTextLocation: EditText
    lateinit var buttonDate: ImageButton
    lateinit var buttonTo: ImageButton
    private lateinit var buttonFrom: ImageButton
    lateinit var buttonExit: ImageButton
    lateinit var textFrom: TextView
    lateinit var textTo: TextView
    lateinit var timeFrom: String
    lateinit var timeTo: String
    lateinit var btnSave: Button
    lateinit var btnDelete: Button
    private lateinit var textDate: TextView
    private lateinit var spinnerNotification: Spinner
    private lateinit var spinnerRepeat: Spinner
    private lateinit var dateDb: String
    private var friends = ArrayList<Int>()
    private lateinit var notificationDb: String
    private lateinit var repeatDb: String
    private lateinit var friendsRecycler: RecyclerView
    var id: Int? = 0
    private var dataFriends = ArrayList<GetFriendResponse>()
    private lateinit var notificationList: Array<String>
    private lateinit var repeatList: Array<String>
    private var TAG = "edit_schedule"
    private var friendsChoose = ArrayList<GetFriendResponse>()
    private var friendsDb = ArrayList<Int>()
    private var timeFromRecom = ""
    private var timeToRecom = ""
    private lateinit var updatedAt: String
    private lateinit var oldTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_schedule)

        initComponent()
        fetchData()

        buttonFrom.setOnClickListener {
            openTimePickerFrom()
        }

        buttonExit.setOnClickListener {
            startActivity(Intent(applicationContext, CalendarActivity::class.java))
        }

        btnSave.setOnClickListener {
            updateSchedule()
        }

        buttonTo.setOnClickListener {
            openTImePickerTo()
        }

        btnDelete.setOnClickListener {
            deleteSchedule()
        }

        buttonDate.setOnClickListener{
            openDatePicker()
        }

        autoCompleteRecom.setOnClickListener {
            getRecomendation()
            Log.d(TAG, "OncClick autocomplet")
        }
    }

    private fun fetchData() {
        val data = intent.getParcelableExtra<SchedulesItem>("schedule_data")


        getFriends()

        if(data != null) {
            id = data.schedule?.id
            editTextTitle.setText(data.schedule?.title)
            editTextDescription.setText(data.schedule?.description)
            editTextLocation.setText(data.schedule?.location)
            dateDb = data.schedule?.date.toString()
            timeFrom = data.schedule?.startTime.toString()
            timeTo = data.schedule?.endTime.toString()
            notificationDb = data.schedule?.notification.toString()
            repeatDb = data.schedule?.repeat.toString()
            textDate.text = convertCalendar(dateDb)
            textFrom.text = convertTime(timeFrom)
            textTo.text = convertTime(timeTo)
            oldTitle = data.schedule?.title.toString()
            updatedAt = data.schedule?.updatedAt.toString()
            Log.d(TAG, "ini : ${data.member}")
            if(data.member != null) {
                for (i in data.member) {
                    friendsDb.add(i?.id!!.toInt())
                }
            }

        }
        setSpinner(notificationDb, repeatDb)
    }

    private fun setAutoCompleteFriends(body: List<GetFriendResponse>) {
        val adapter = AutoCompleteFriendAdapter(
            this, R.layout.friends_layout,
            body as MutableList<GetFriendResponse>
        )

        editTextPeople.setAdapter(adapter)

        adapter.setOnFriendsClickCallback(object :
            AutoCompleteFriendAdapter.OnFriendsClickCallback {
            override fun onClickFriends(data: GetFriendResponse) {
                friendsDb.add(data.id.toInt())
                friendsChoose.add(data)
                editTextPeople.setText(data.username, false)
                showRecyclist(friendsChoose)
                Log.d(TAG, "friends DB: ${friendsDb}")
            }
        })
    }

    fun showRecyclist(friends: List<GetFriendResponse>) {
        val friendAdapter = FriendsAdapterSchedule(friends as ArrayList<GetFriendResponse>)

        friendsRecycler.setHasFixedSize(true)
        friendsRecycler.layoutManager = LinearLayoutManager(this)
        friendsRecycler.adapter = friendAdapter

        friendAdapter.setOnFriendsRecyclerClickCallback(object :
            FriendsAdapterSchedule.OnFriendsRecyclerClickCallback {
            override fun onClickItem(data: GetFriendResponse) {
                friendsDb.remove(data.id.toInt())
                friendsChoose.remove(data)
                showRecyclist(friendsChoose)
            }

        })
    }

    private fun setSpinner(notification: String, repeat: String) {
        notificationList = resources.getStringArray(R.array.notification)
        repeatList = resources.getStringArray(R.array.repeat)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, this.notificationList
        )

        val adapterRepeat = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, this.repeatList
        )

        val postitionNotification = adapter.getPosition(notification)
        val positionRepeat = adapterRepeat.getPosition(repeat)

        spinnerNotification.adapter = adapter
        spinnerRepeat.adapter = adapterRepeat
        spinnerNotification.setSelection(postitionNotification)
        spinnerRepeat.setSelection(positionRepeat)
    }

    private fun convertCalendar(date: String): String {
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateFormatView = SimpleDateFormat("EEE, d MMM yyyy", Locale.US)

        return dateFormatView.format(dateFormatDB.parse(date))
    }

    private fun convertTime(time: String): String {
        val timeFormatDB = SimpleDateFormat("HH:mm:ss", Locale.US)
        val timeFormatView = SimpleDateFormat("hh:mm a", Locale.US)

        return timeFormatView.format(timeFormatDB.parse(time))
    }

    private fun initComponent() {
        spinnerRepeat = findViewById(R.id.repeat_repeat)
        spinnerNotification = findViewById(R.id.notification_repeat)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextPeople = findViewById(R.id.auto_complete_friends_edit_schedule)
        editTextLocation = findViewById(R.id.edit_text_location)
        buttonFrom = findViewById(R.id.button_from_time_edit_delete_schedule)
        buttonTo = findViewById(R.id.button_to_edit_delete_schedule)
        buttonDate = findViewById(R.id.button_date_edit_delete_schedule)
        textFrom = findViewById(R.id.edit_text_time_from_edit_delete_schedule)
        textTo = findViewById(R.id.edit_text_to_edit_delete_schedule)
        textDate = findViewById(R.id.edit_text_date_edit_delete_schedule)
        friendsRecycler = findViewById(R.id.recyclerView_friends_edit_schedule)
        buttonExit = findViewById(R.id.button_exit)
        btnSave = findViewById(R.id.save)
        btnDelete = findViewById(R.id.delete)
        autoCompleteRecom = findViewById(R.id.auto_complete_recomendation_edit_schedule)
    }

    fun cancelAlarm() {
        val intent = Intent(applicationContext, AlarmBroadcast::class.java)
        val p = getSystemService(ALARM_SERVICE) as AlarmManager
        val id = getIdAlarm()
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id.toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        p.cancel(pendingIntent)
    }

    fun getIdAlarm(): Long{
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val timeUpdatedAt = format.parse(updatedAt).time
        var hasil = 0

        for(i in oldTitle) {
            hasil += i.code
        }

        return (timeUpdatedAt+hasil)
    }

    private fun getFriends() {
        val apiClient = APIClient()

        apiClient.getApiService(this).getFriends()
            .enqueue(object : Callback<List<GetFriendResponse>> {
                override fun onResponse(
                    call: Call<List<GetFriendResponse>>,
                    response: Response<List<GetFriendResponse>>
                ) {
                    if (response.code() == 200) {
                        response.body()?.let {
                            dataFriends.addAll(it)
                        }
                        for(j in dataFriends) {
                            for(i in friendsDb) {
                                if(j.id.toInt() == i) {
                                    friendsChoose.add(j)
                                }
                            }
                            Log.d(TAG, "$j")
                            Log.d(TAG, "$friendsChoose")
                        }
                        showRecyclist(friendsChoose)
//                        autocomplete.setAdapter(AutoCompleteFriendAdapter(this@AddScheduleActivity, R.layout.friends_layout, dataFriends))
                        response.body()?.let { setAutoCompleteFriends(it) }
                        Log.d(TAG, "get friends : $dataFriends")
                    }
                }

                override fun onFailure(call: Call<List<GetFriendResponse>>, t: Throwable) {
                    Log.d(TAG, "error get Friends : $t")
                }

            })
    }

    fun getRecomendation() {
        val apiCLient = APIClient()
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        dateDb = dateFormatDB.format(dateFormatDB.parse(dateDb))
        val timeFormatDb = SimpleDateFormat("HH:mm", Locale.US)

        timeTo = timeFormatDb.format(timeFormatDb.parse(timeTo))
        timeFrom = timeFormatDb.format(timeFormatDb.parse(timeFrom))

        apiCLient.getApiService(this).getRecomendation(dateDb, timeFrom, timeTo, friendsDb)
            .enqueue(object : Callback<RecomendationResponse> {
                override fun onResponse(
                    call: Call<RecomendationResponse>,
                    response: Response<RecomendationResponse>
                ) {
                    if(response.code() == 200) {
                        if(response.body()?.rekomendasi != null) {
                            setAutoCompleteRecomendation(response.body()!!.rekomendasi)
                        }
                    }
                }

                override fun onFailure(call: Call<RecomendationResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun setAutoCompleteRecomendation(rekomendasi: List<RekomendasiItem>) {
        val adapter = AutoCompleteRecomAdapter(
            this, R.layout.recomendation_layout,
            rekomendasi as MutableList<RekomendasiItem>
        )

        autoCompleteRecom.setAdapter(adapter)
        autoCompleteRecom.showDropDown()

        adapter.setOnRecomClickCallback(object: AutoCompleteRecomAdapter.OnRecomCLickCallback {
            @SuppressLint("SetTextI18n")
            override fun onCLickRecom(data: RekomendasiItem) {
                timeFromRecom = data.startTime
                timeToRecom = data.endTime
                autoCompleteRecom.setText("From : ${data.startTime} To : ${data.endTime}", false)
            }

        })
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val dateFormatView = SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH)
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        val dp = DatePickerDialog(this, {view, mYear, mMonth, mDay ->
            dateDb = "${mYear}-${mMonth + 1}-${mDay}"

            textDate.text = dateFormatView.format(dateFormatDB.parse(dateDb))
        }, year, month, date)

        dp.show()
    }

    private fun openTimePickerFrom() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timeFormatView = SimpleDateFormat("h:mm a", Locale.US)
        val timeFormatDb = SimpleDateFormat("HH:mm", Locale.US)

        val tpf = TimePickerDialog(this, { view, h, m ->
            calendar.set(Calendar.HOUR_OF_DAY, h)
            calendar.set(Calendar.MINUTE, m)

            timeFrom = timeFormatDb.format(calendar.time)
            textFrom.text = timeFormatView.format(timeFormatDb.parse(timeFrom))
        },hour,minute,false)

        tpf.show()
    }

    private fun openTImePickerTo() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timeFormatView = SimpleDateFormat("h:mm a", Locale.US)
        val timeFormatDb = SimpleDateFormat("HH:mm", Locale.US)

        val tpt = TimePickerDialog(this, { view, h, m ->
            calendar.set(Calendar.HOUR_OF_DAY, h)
            calendar.set(Calendar.MINUTE, m)

            timeTo = timeFormatDb.format(calendar.time)
            textTo.text = timeFormatView.format(timeFormatDb.parse(timeTo))
        },hour,minute,false)

        tpt.show()
    }

    fun updateSchedule() {
        val apiClient = APIClient()
        val title = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val location = editTextLocation.text.toString()
        val repeat = spinnerRepeat.selectedItem.toString()
        val notification = spinnerNotification.selectedItem.toString()
        val myIntent = Intent(this, CalendarActivity::class.java)
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val timeFormatDb = SimpleDateFormat("HH:mm", Locale.US)
        dateDb = dateFormatDB.format(dateFormatDB.parse(dateDb))
        timeFrom = timeFormatDb.format(timeFormatDb.parse(timeFrom))
        timeTo = timeFormatDb.format(timeFormatDb.parse(timeTo))


        if(autoCompleteRecom.text.isNotEmpty()) {
            if(timeFromRecom.isNotEmpty() && timeToRecom.isNotEmpty()) {
                timeFrom = timeFormatDb.format(timeFormatDb.parse(timeFromRecom))
                timeTo = timeFormatDb.format(timeFormatDb.parse(timeToRecom))
            }
        }

        Log.d(TAG, "friends input : $friendsDb")

        id?.let {
            apiClient.getApiService(this).updateSchedule(
                it,
                title,
                description,
                location,
                dateDb,
                timeFrom,
                timeTo,
                notification,
                repeat,
                friendsDb)
                .enqueue(object: Callback<UpdateScheduleResponse>{
                    override fun onResponse(
                        call: Call<UpdateScheduleResponse>,
                        response: Response<UpdateScheduleResponse>
                    ) {
                        if(response.code().equals(200)) {
                            Log.d("update schedule", "sukses")
                            Toast.makeText(applicationContext, "schedule berhasil di update", Toast.LENGTH_SHORT).show()
                            cancelAlarm()
                            startActivity(myIntent)
                        } else {
                            Log.d("update schedule", "gagal")
                        }
                    }

                    override fun onFailure(call: Call<UpdateScheduleResponse>, t: Throwable) {
                        Log.d("update schedule", "$t")
                    }
                })
        }
    }

    fun deleteSchedule() {
        val apiClient = APIClient()
        var myIntent = Intent(this, CalendarActivity::class.java)

        id?.let {
            apiClient.getApiService(this).deleteSchedule(it)
                .enqueue(object: Callback<DeleteScheduleResponse>{
                    override fun onResponse(
                        call: Call<DeleteScheduleResponse>,
                        response: Response<DeleteScheduleResponse>
                    ) {
                        if(response.code().equals(202)) {
                            Toast.makeText(applicationContext, "schedule berhasil di delete", Toast.LENGTH_SHORT).show()
                            cancelAlarm()
                            startActivity(myIntent)
                        }
                    }

                    override fun onFailure(call: Call<DeleteScheduleResponse>, t: Throwable) {
                        Log.d("error delete", "${t}")
                    }
                })
        }
    }
}