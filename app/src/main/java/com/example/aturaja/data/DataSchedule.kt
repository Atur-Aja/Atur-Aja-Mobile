package com.example.aturaja.data

import android.content.Context
import com.example.aturaja.model.GetScheduleResponse
import com.example.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataSchedule {
    private var apiClient = APIClient()
    private var schedule = ArrayList<GetScheduleResponse>()

//    fun loadScheduleDB(context: Context, username: String): ArrayList<GetScheduleResponse> {
//        apiClient.getApiService(context).getSchedules()
//            .enqueue(object: Callback<ArrayList<GetScheduleResponse>> {
//                override fun onResponse(
//                    call: Call<ArrayList<GetScheduleResponse>>,
//                    response: Response<ArrayList<GetScheduleResponse>>
//                ) {
//                    response.body()?.let { schedule.addAll(it) }
//                }
//
//                override fun onFailure(call: Call<ArrayList<GetScheduleResponse>>, t: Throwable) {
//                }
//            })
//
//        return schedule
//    }

//    fun sizeSchduleDb(context: Context, username: String): Int {
//        apiClient.getApiService(context).getSchedules(username)
//            .enqueue(object: Callback<ArrayList<GetScheduleResponse>> {
//                override fun onResponse(
//                    call: Call<ArrayList<GetScheduleResponse>>,
//                    response: Response<ArrayList<GetScheduleResponse>>
//                ) {
//                    response.body()?.let { schedule.addAll(it) }
//                }
//
//                override fun onFailure(call: Call<ArrayList<GetScheduleResponse>>, t: Throwable) {
//                }
//
//            })
//
//        return schedule.size
//    }
}