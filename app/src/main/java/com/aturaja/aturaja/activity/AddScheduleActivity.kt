package com.aturaja.aturaja.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.AutoCompleteFriendAdapter
import com.aturaja.aturaja.adapter.AutoCompleteRecomAdapter
import com.aturaja.aturaja.adapter.FriendsAdapterSchedule
import com.aturaja.aturaja.model.*
import com.aturaja.aturaja.network.APIClient
import com.aturaja.aturaja.session.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddScheduleActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var buttonDate: ImageButton
    private lateinit var buttonTimeTo: ImageButton
    private lateinit var buttonTimeFrom: ImageButton
    private lateinit var buttonExit: ImageButton
    private lateinit var buttonFriends: ImageButton
    private lateinit var textFrom: TextView
    private lateinit var textTo: TextView
    private lateinit var textDate: TextView
    private lateinit var spinnerNotification: Spinner
    private lateinit var spinnerRepeat: Spinner
    private lateinit var autocomplete: AutoCompleteTextView
    private lateinit var autoCompleteRecomendation: AutoCompleteTextView
    private lateinit var recyclerView: RecyclerView


    private var timeFrom = ""
    private var timeTo = ""
    private var  dateDb= ""
    private var timeFromRecom = ""
    private var timeToRecom = ""
    private var dateRecom = ""

    private var bitmapArray = ArrayList<Bitmap>()
    private var bitmapArrayData = ArrayList<Bitmap>()
    private var friendsDb = ArrayList<Int>()
    private var friendsChoose = ArrayList<GetFriendResponse>()
    private var dataFriends = ArrayList<GetFriendResponse>()
    private var arrayRecycler = ArrayList<ArrayFriendSchedule>()

    private val TAG = "addSchedule"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        initComponent()
        setSpinner()
        getFriends()


        autoCompleteRecomendation.setOnClickListener {
            getRecomendation()
        }


        buttonDate.setOnClickListener {
            openDatePicker()
        }

        buttonTimeTo.setOnClickListener {
            openTImePickerTo()
        }

        buttonTimeFrom.setOnClickListener {
            openTimePickerFrom()
        }

        buttonExit.setOnClickListener {
            startActivity(Intent(applicationContext, CalendarActivity::class.java))
        }
    }

    private fun setSpinner() {
        val notification = resources.getStringArray(R.array.notification)
        val repeat = resources.getStringArray(R.array.repeat)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, notification
        )

        val adapterRepeat = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, repeat
        )

        spinnerNotification.adapter = adapter
        spinnerRepeat.adapter = adapterRepeat


    }

    private fun initComponent() {
        spinnerRepeat = findViewById(R.id.repeat_repeat)
        spinnerNotification = findViewById(R.id.notification_repeat)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextLocation = findViewById(R.id.edit_text_location)
        buttonDate = findViewById(R.id.button_date_add_schedule)
        buttonTimeFrom = findViewById(R.id.button_from_time)
        buttonTimeTo = findViewById(R.id.button_to_time)
        textFrom = findViewById(R.id.edit_text_time_from)
        textTo = findViewById(R.id.edit_text_time_to)
        textDate = findViewById(R.id.edit_text_date)
        buttonExit = findViewById(R.id.button_exit)
        autocomplete = findViewById(R.id.auto_complete_add_schedule)
        autoCompleteRecomendation = findViewById(R.id.auto_complete_recomendation_add_schedule)
        recyclerView = findViewById(R.id.recyclerView_addSchedule)
    }

    private fun setAutoComplete(body: List<GetFriendResponse>) {
        val adapter = AutoCompleteFriendAdapter(
            this, R.layout.friends_layout,
            body as MutableList<GetFriendResponse>
        )

        autocomplete.setAdapter(adapter)

        adapter.setOnFriendsClickCallback(object :
            AutoCompleteFriendAdapter.OnFriendsClickCallback {
            override fun onClickFriends(data: GetFriendResponse) {
                checkFriends(data)
                autocomplete.setText(data.username, false)
            }
        })
    }

    private fun checkFriends(data: GetFriendResponse) {
        if(friendsDb.contains(data.id.toInt())) {
            Toast.makeText(this, "Username sudah ditambahkan", Toast.LENGTH_SHORT).show()
        } else {
            friendsDb.add(data.id.toInt())
            friendsChoose.add(data)
            getImageUser(data)
        }

        Log.d(TAG, "bitmap : ${bitmapArray.size}")
//
    }

    private fun setAutoCompleteRecomendation(rekomendasi: List<RekomendasiItem>) {
        if(rekomendasi.isNotEmpty()) {
            val adapter = AutoCompleteRecomAdapter(
                this, R.layout.recomendation_layout,
                rekomendasi as MutableList<RekomendasiItem>
            )

            autoCompleteRecomendation.setAdapter(adapter)
            autoCompleteRecomendation.showDropDown()

            adapter.setOnRecomClickCallback(object: AutoCompleteRecomAdapter.OnRecomCLickCallback {
                @SuppressLint("SetTextI18n")
                override fun onCLickRecom(data: RekomendasiItem) {
                    timeFromRecom = data.startTime
                    timeToRecom = data.endTime
                    dateRecom = data.date
                    autoCompleteRecomendation.setText("Date : ${data.date}\n\nFrom : ${data.startTime} To : ${data.endTime}", false)
                }

            })
        }
    }

    fun getRecomendation() {
        if(dateDb.isEmpty() || timeFrom.isEmpty() || timeTo.isEmpty() || friendsDb.isNullOrEmpty()) {
            Toast.makeText(this, "input date, time and friends", Toast.LENGTH_SHORT).show()
        } else {
            val apiCLient = APIClient()
            val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            dateDb = dateFormatDB.format(dateFormatDB.parse(dateDb))

            Log.d(TAG, "date : $dateDb")
            Log.d(TAG, "timeFrom : $timeFrom")
            Log.d(TAG, "timeTo : $timeTo")
            Log.d(TAG, "friends: $friendsDb")

            apiCLient.getApiService(this).getRecomendation(dateDb, timeFrom, timeTo, friendsDb)
                .enqueue(object : Callback<RecomendationResponse> {
                    override fun onResponse(
                        call: Call<RecomendationResponse>,
                        response: Response<RecomendationResponse>
                    ) {
                        if(response.code() == 200) {
                            response.body()?.let { setAutoCompleteRecomendation(it.rekomendasi) }
                        } else if(response.code() == 401){
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            SessionManager(applicationContext).clearTokenAndUsername()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<RecomendationResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    fun showRecyclist() {
        val friendAdapter = FriendsAdapterSchedule(arrayRecycler)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = friendAdapter

        friendAdapter.setOnFriendsRecyclerClickCallback(object :
            FriendsAdapterSchedule.OnFriendsRecyclerClickCallback {
            override fun onClickItem(data: ArrayFriendSchedule) {
                friendsDb.remove(data.data.id.toInt())
                arrayRecycler.remove(data)
                showRecyclist()
            }

        })
    }

    private fun getImageUser(data: GetFriendResponse) {
        val apiClient = APIClient()
        var bitmap: Bitmap

        apiClient.getApiService(this).getPhoto(data.photo.toString())
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.code() == 200) {
                        bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                        val model = ArrayFriendSchedule(data, bitmap)
                        arrayRecycler.add(model)
                        showRecyclist()
                    } else if(response.code() == 401){
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        SessionManager(applicationContext).clearTokenAndUsername()
                        finish()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "Error : $t")
                }

            })
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val dateFormatView = SimpleDateFormat("EEE, d MMM yyyy", Locale.US)
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val dp = DatePickerDialog(this, { _, mYear, mMonth, mDay ->
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

        val tpf = TimePickerDialog(this, { _, h, m ->
            calendar.set(Calendar.HOUR_OF_DAY, h)
            calendar.set(Calendar.MINUTE, m)

            timeFrom = timeFormatDb.format(calendar.time)
            textFrom.text = timeFormatView.format(timeFormatDb.parse(timeFrom))
        }, hour, minute, false)

        tpf.show()
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
                        response.body()?.let { dataFriends.addAll(it) }
//                        autocomplete.setAdapter(AutoCompleteFriendAdapter(this@AddScheduleActivity, R.layout.friends_layout, dataFriends))
                        response.body()?.let {  setAutoComplete(it)}
                    }
                }

                override fun onFailure(call: Call<List<GetFriendResponse>>, t: Throwable) {
                    Log.d(TAG, "error get Friends : $t")
                }

            })
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
        }, hour, minute, false)

        tpt.show()
    }

    fun saveScheduleClick(view: View) {
        val title = editTextTitle.text.toString()
        val apiClient = APIClient()
        val myIntent = Intent(this, ListScheduleActivity::class.java)
        val description = editTextDescription.text.toString()
        val location = editTextLocation.text.toString()
        val repeat = spinnerRepeat.selectedItem.toString()
        val notification = spinnerNotification.selectedItem.toString()
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val timeFormatDb = SimpleDateFormat("HH:mm", Locale.US)
        val responseFail = "end time must be an hour after start time"

        if(autoCompleteRecomendation.text.trim().isNotEmpty()) {
            if(timeFromRecom.isNotEmpty() && timeToRecom.isNotEmpty()) {
                timeFrom = timeFormatDb.format(timeFormatDb.parse(timeFromRecom))
                timeTo = timeFormatDb.format(timeFormatDb.parse(timeToRecom))
                dateDb = dateFormatDB.format(dateFormatDB.parse(dateRecom))
            }
        }
        if(title.isEmpty() || dateDb.isEmpty() || timeFrom.isEmpty() || timeTo.isEmpty()) {
            Toast.makeText(this, "masukkan judul, tanggl dan waktu", Toast.LENGTH_SHORT).show()
        } else {
            dateDb = dateFormatDB.format(dateFormatDB.parse(dateDb))
            apiClient.getApiService(this).createSchedules(
                title,
                description,
                location,
                dateDb,
                timeFrom,
                timeTo,
                notification,
                repeat,
                friendsDb
            )
                .enqueue(object : Callback<CreateScheduleResponse> {
                    override fun onResponse(
                        call: Call<CreateScheduleResponse>,
                        response: Response<CreateScheduleResponse>
                    ) {
                        val responseCreate = response.body()?.message
                        if (responseCreate.equals("schedule created successfully")) {
                            Toast.makeText(applicationContext, responseCreate, Toast.LENGTH_LONG).show()
                            startActivity(myIntent)
                        } else if(response.code() == 401){
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            SessionManager(applicationContext).clearTokenAndUsername()
                            finish()
                        } else  {
                            Toast.makeText(applicationContext, responseFail, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<CreateScheduleResponse>, t: Throwable) {
                        Log.d("error create", "$t")
                    }
                })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ListScheduleActivity::class.java))
    }
}
