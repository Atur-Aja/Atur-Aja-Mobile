package com.example.aturaja.activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.aturaja.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


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
    var dateFrom: String? = null
    var dateTo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        val itemsRepeat = listOf("Daily", "Weekly", "Monthly", "Never")
        val adapterRepeat = ArrayAdapter(requireContext(), R.layout.dropdown_repeat, itemsRepeat)
        val itemsNotification = listOf("5 minute", "10 minute", "15 minute")
        val adapterNotification = ArrayAdapter(requireContext(), R.layout.dropdown_notification, itemsNotification)
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

        (dropdownRepeat.editText as? AutoCompleteTextView)?.setAdapter(adapterRepeat)
        (dropdownNotification.editText as? AutoCompleteTextView)?.setAdapter(adapterNotification)

        buttonFrom.setOnClickListener {
           openDateTimePickerFrom()
        }

        buttonExit.setOnClickListener {
            val myIntent = Intent(applicationContext, HomeActivity::class.java)

            startActivity(myIntent)
        }

        buttonTo.setOnClickListener {
            openDateTimePickerTo()
        }
    }

    private fun requireContext(): Context {
        return applicationContext
    }

    private fun openDateTimePickerFrom() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
            val mMonth = mMonth+1
            dateFrom = "${mDay}-${mMonth}-${mYear}"
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                 timeFrom = SimpleDateFormat("HH:mm").format(calendar.time)
                textFrom.setText("${dateFrom} ${timeFrom}")
            }
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }, year, month, date)

        dpd.show()
    }

    private fun openDateTimePickerTo() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
            val mMonth = mMonth+1
            dateTo = "${mDay}-${mMonth}-${mYear}"
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                timeTo = SimpleDateFormat("HH:mm").format(calendar.time)
                textTo.setText("${dateTo} ${timeTo}")
            }
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }, year, month, date)

        dpd.show()
    }
}
