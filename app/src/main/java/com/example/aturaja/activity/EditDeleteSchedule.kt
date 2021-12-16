package com.example.aturaja.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.aturaja.R
import com.example.aturaja.model.DeleteScheduleResponse
import com.example.aturaja.model.SchedulesItem
import com.example.aturaja.model.UpdateScheduleResponse
import com.example.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditDeleteSchedule : AppCompatActivity() {
    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText
    lateinit var editTextPeople: EditText
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
    var id: Int? = 0
    private lateinit var notification: Array<String>
    private lateinit var repeat: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_schedule)

        notification = resources.getStringArray(R.array.notification)
        repeat = resources.getStringArray(R.array.repeat)
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
    }

    private fun fetchData() {
        val data = intent.getParcelableExtra<SchedulesItem>("schedule_data")

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
        }

        setSpinner(notificationDb, repeatDb)
    }

    private fun setSpinner(notification: String, repeat: String) {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, this.notification
        )

        val adapterRepeat = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, this.repeat
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
        editTextPeople = findViewById(R.id.edit_text_people)
        editTextLocation = findViewById(R.id.edit_text_location)
        buttonFrom = findViewById(R.id.button_from_time_edit_delete_schedule)
        buttonTo = findViewById(R.id.button_to_edit_delete_schedule)
        buttonDate = findViewById(R.id.button_date_edit_delete_schedule)
        textFrom = findViewById(R.id.edit_text_time_from_edit_delete_schedule)
        textTo = findViewById(R.id.edit_text_to_edit_delete_schedule)
        textDate = findViewById(R.id.edit_text_date_edit_delete_schedule)
        buttonExit = findViewById(R.id.button_exit)
        btnSave = findViewById(R.id.save)
        btnDelete = findViewById(R.id.delete)
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

        id?.let {
            apiClient.getApiService(this).updateSchedule(it, title, description, location, dateDb, timeFrom, timeTo, notification, repeat, friends)
                .enqueue(object: Callback<UpdateScheduleResponse>{
                    override fun onResponse(
                        call: Call<UpdateScheduleResponse>,
                        response: Response<UpdateScheduleResponse>
                    ) {
                        if(response.code().equals(200)) {
                            Log.d("update schedule", "sukses")
                            Toast.makeText(applicationContext, "schedule berhasil di update", Toast.LENGTH_SHORT).show()
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