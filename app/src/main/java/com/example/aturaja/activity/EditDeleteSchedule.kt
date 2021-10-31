package com.example.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.*
import com.example.aturaja.R
import com.example.aturaja.model.UpdateScheduleResponse
import com.example.aturaja.network.APIClient
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class EditDeleteSchedule : AppCompatActivity() {
    lateinit var dropdownRepeat: TextInputLayout
    lateinit var dropdownNotification: TextInputLayout
    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText
    lateinit var editTextPeople: EditText
    lateinit var editTextLocation: EditText
    lateinit var buttonFrom: ImageButton
    lateinit var buttonTo: ImageButton
    lateinit var buttonExit: ImageButton
    lateinit var textFrom: TextView
    lateinit var textTo: TextView
    lateinit var timeFrom: String
    lateinit var timeTo: String
    val itemsRepeat = listOf("Daily", "Weekly", "Monthly", "Never")
    val itemsNotification = listOf("5 minute", "10 minute", "15 minute")
    lateinit var dateFrom: String
    lateinit var dateTo: String
    var dateView: String? = null
    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_schedule)

        val adapterRepeat = ArrayAdapter(applicationContext, R.layout.dropdown_repeat, itemsRepeat)
        val adapterNotification =
            ArrayAdapter(applicationContext, R.layout.dropdown_notification, itemsNotification)

        initComponent()

        (dropdownRepeat.editText as? AutoCompleteTextView)?.setAdapter(adapterRepeat)
        (dropdownNotification.editText as? AutoCompleteTextView)?.setAdapter(adapterNotification)

        fetchData()

        buttonFrom.setOnClickListener {
            openDateTimePickerFrom()
        }

        buttonExit.setOnClickListener {
            startActivity(Intent(applicationContext, CalendarActivity::class.java))
        }

        buttonTo.setOnClickListener {
            openDateTimePickerTo()
        }
    }

    private fun fetchData() {
        var data = intent.extras
        var dateFrom: String
        var dateTo: String

        if(data != null) {
            id = data.getString("id").toString()
            dateFrom = data.getString("startDate").toString()
            dateTo = data.getString("endDate").toString()
            editTextTitle.setText(data.getString("title"))
            textFrom.text = "${convertCalendar(dateFrom)} ${data.getString("startTime")}"
            textTo.text = "${convertCalendar(dateTo)} ${data.getString("endTime")}"
        }
    }

    private fun convertCalendar(date: String): String {
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatView = SimpleDateFormat("EEE, d MMM yyyy")

        return dateFormatView.format(dateFormatDB.parse(date))
    }

    private fun initComponent() {
        dropdownRepeat = findViewById(R.id.text_repeat)
        dropdownNotification = findViewById(R.id.notification_repeat)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextPeople = findViewById(R.id.edit_text_people)
        editTextLocation = findViewById(R.id.edit_text_location)
        buttonFrom = findViewById(R.id.button_from)
        buttonTo = findViewById(R.id.button_to)
        textFrom = findViewById(R.id.edit_text_from)
        textTo = findViewById(R.id.edit_text_to)
        buttonExit = findViewById(R.id.button_exit)
    }

    fun openDateTimePickerFrom() {
        val dateFormatCalendar = SimpleDateFormat("MMM dd, yyyy")
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatView = SimpleDateFormat("EEE, d MMM yyyy")
        val timeFormatDB = SimpleDateFormat("hh:mm")
        val timeFormatView = SimpleDateFormat("hh:mm a")
        var dateView: String

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .setTitleText("waktu terus bergulir")
            .build()

        datePicker.show(supportFragmentManager, "DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener {
            dateView = (dateFormatView.format(dateFormatCalendar.parse(datePicker.headerText)))
            dateFrom = (dateFormatDB.format(dateFormatCalendar.parse(datePicker.headerText)))

            timePicker.show(supportFragmentManager, "tag");
            timePicker.addOnPositiveButtonClickListener {
                val time =
                    timeFormatView.format(timeFormatDB.parse("${timePicker.hour}:${timePicker.minute}"))
                timeFrom = "${timePicker.hour}:${timePicker.minute}"
                textFrom.setText("${dateView} ${time}")
            }
        }
    }

    fun openDateTimePickerTo() {
        val dateFormatCalendar = SimpleDateFormat("MMM dd, yyyy")
        val dateFormatDB = SimpleDateFormat("yyyy-MM-dd")
        val dateFormatView = SimpleDateFormat("EEE, d MMM yyyy")
        val timeFormatDB = SimpleDateFormat("hh:mm")
        val timeFormatView = SimpleDateFormat("hh:mm a")
        var dateView: String

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .setTitleText("waktu terus bergulir")
            .build()

        datePicker.show(supportFragmentManager, "DATE_PICKER")
        datePicker.addOnPositiveButtonClickListener {
            dateView = (dateFormatView.format(dateFormatCalendar.parse(datePicker.headerText)))
            dateTo = (dateFormatDB.format(dateFormatCalendar.parse(datePicker.headerText)))

            timePicker.show(supportFragmentManager, "tag");
            timePicker.addOnPositiveButtonClickListener {
                val time =
                    timeFormatView.format(timeFormatDB.parse("${timePicker.hour}:${timePicker.minute}"))
                timeTo = "${timePicker.hour}:${timePicker.minute}"
                textTo.setText("${dateView} ${time}")
            }
        }
    }

    fun btnSaveClick() {
        val apiClient = APIClient()
        var title = editTextTitle.text.toString()

        apiClient.getApiService(this).updateSchedule(id, title, dateFrom, timeFrom, dateTo, timeTo)
            .enqueue(object: Callback<UpdateScheduleResponse>{
                override fun onResponse(
                    call: Call<UpdateScheduleResponse>,
                    response: Response<UpdateScheduleResponse>
                ) {
                    if(response.code().equals(201)) {

                    }
                }

                override fun onFailure(call: Call<UpdateScheduleResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun btnDeleteClick() {

    }
}