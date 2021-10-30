package com.example.aturaja.activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.aturaja.R
import com.example.aturaja.model.CreateScheduleResponse
import com.example.aturaja.network.APIClient
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class AddScheduleActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        val adapterRepeat = ArrayAdapter(applicationContext, R.layout.dropdown_repeat, itemsRepeat)
        val adapterNotification =
            ArrayAdapter(applicationContext, R.layout.dropdown_notification, itemsNotification)

        initComponent()

        (dropdownRepeat.editText as? AutoCompleteTextView)?.setAdapter(adapterRepeat)
        (dropdownNotification.editText as? AutoCompleteTextView)?.setAdapter(adapterNotification)

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

    private fun openDateTimePickerFrom() {
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

    private fun openDateTimePickerTo() {
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

    fun saveScheduleClick(view: View) {
        var title = editTextTitle.text.toString()
        var apiClient = APIClient()

        apiClient.getApiService(this).createSchedules(title, dateFrom, timeFrom, dateTo, timeTo)
            .enqueue(object : Callback<CreateScheduleResponse> {
                override fun onResponse(
                    call: Call<CreateScheduleResponse>,
                    response: Response<CreateScheduleResponse>
                ) {
                    var responseCreate = response.body()?.message
                    if(responseCreate.equals("schedule created successfully")) {
                        Toast.makeText(applicationContext, responseCreate, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, responseCreate, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CreateScheduleResponse>, t: Throwable) {
                    Log.d("error create", "$t")
                }
            })
    }
}

//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val date = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
//            val mMonth = mMonth+1
//            var sdf = SimpleDateFormat("yyyy-MM-dd")
//            var sdfa = SimpleDateFormat("EEE, d MMM yyyy")
//            var dateView = sdfa.format(sdf.parse("${mYear}-${mMonth}-${mDay}"))
//            dateFrom = "${mYear}-${mMonth}-${mDay}"
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                calendar.set(Calendar.MINUTE, minute)
//
//                 timeFrom = SimpleDateFormat("HH:mm").format(calendar.time)
//                textFrom.setText("${dateView} ${timeFrom}")
//            }
//            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
//        }, year, month, date)
//
//        dpd.show()

//    private fun openDateTimePickerTo() {
//
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val date = calendar.get(Calendar.DAY_OF_MONTH)
//
//        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
//            val mMonth = mMonth+1
//            var sdf = SimpleDateFormat("yyyy-MM-dd")
//            var sdfa = SimpleDateFormat("EEE, d MMM yyyy")
//            var dateView = sdfa.format(sdf.parse("${mYear}-${mMonth}-${mDay}"))
//            dateTo = "${mYear}-${mMonth}-${mDay}"
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                calendar.set(Calendar.MINUTE, minute)
//
//                timeTo = SimpleDateFormat("HH:mm").format(calendar.time)
//                textTo.setText("${dateView} ${timeTo}")
//            }
//            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
//        }, year, month, date)
//
//        dpd.show()
//    }
//}
