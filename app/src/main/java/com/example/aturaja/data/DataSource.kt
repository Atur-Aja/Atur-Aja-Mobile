package com.example.aturaja.data

import android.content.Context
import com.example.aturaja.R
import com.example.aturaja.model.GetScheduleResponse
import com.example.aturaja.model.ListSchedule
import com.example.aturaja.network.APIClient

class DataSource() {
    fun loadSchedule(): List<ListSchedule> {
        return listOf<ListSchedule>(
            ListSchedule(R.string.affirmation1),
            ListSchedule(R.string.affirmation2),
            ListSchedule(R.string.affirmation3)
        )
    }
}