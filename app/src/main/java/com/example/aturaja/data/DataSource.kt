package com.example.aturaja.data

import com.example.aturaja.R
import com.example.aturaja.model.ListSchedule

class DataSource {
    fun loadSchedule(): List<ListSchedule> {
        return listOf<ListSchedule>(
            ListSchedule(R.string.affirmation1),
            ListSchedule(R.string.affirmation2),
            ListSchedule(R.string.affirmation3)
        )
    }
}