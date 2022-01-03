package com.aturaja.aturaja.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.WaitingListAdapter
import com.aturaja.aturaja.databinding.FragmentWaitingListBinding
import com.aturaja.aturaja.model.*
import com.aturaja.aturaja.network.APIClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WaitingListFragment : Fragment(), Updateable {
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<WaitingList>
    private lateinit var friendName : Array<String>
    private lateinit var friendEmail : Array<String>
    private lateinit var friendImage : Array<Int>
    private var arrayData = ArrayList<GetAllFriendRequestResponse>()
    private var bitmapArray = ArrayList<Bitmap>()
    private var arrayRecycler = ArrayList<FriendsRecyclerWaitingList>()
    private var _binding: FragmentWaitingListBinding ?= null
    private val binding get() = _binding!!
    private lateinit var konteks: Context
    private val TAG = "WaitingListFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWaitingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = binding.waitingListRecyclerView

        refresh()
        getData()
    }

    fun refresh() {
        _binding?.swiperefreshWaitingList?.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        val apiClient = APIClient()

        apiClient.getApiService(konteks).getFriendsRequest()
            .enqueue(object: Callback<List<GetAllFriendRequestResponse>> {
                override fun onResponse(
                    call: Call<List<GetAllFriendRequestResponse>>,
                    response: Response<List<GetAllFriendRequestResponse>>
                ) {
                    if(response.code() == 200) {
                        _binding?.swiperefreshWaitingList?.isRefreshing = false
                        arrayData.clear()
                        bitmapArray.clear()
                        response.body()?.let { arrayData.addAll(it)
                            setRecyclerData()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<GetAllFriendRequestResponse>>,
                    t: Throwable
                ) {
                    _binding?.swiperefreshWaitingList?.isRefreshing = false
                    Log.d(TAG, "$t")
                }

            })
    }

    private fun getImageUser(data: GetAllFriendRequestResponse) {
        val apiClient = APIClient()
        var bitmap: Bitmap

        apiClient.getApiService(konteks).getPhoto(data.photo.toString())
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.code() == 200) {
                        _binding?.swiperefreshWaitingList?.isRefreshing = false
                        bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                        var model = FriendsRecyclerWaitingList(data, bitmap)
                        arrayRecycler.add(model)
                        if(arrayRecycler.size == arrayData.size) {
                            showRecyclist()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _binding?.swiperefreshWaitingList?.isRefreshing = false
                    Log.d(TAG, "Error : $t")
                }
            })
    }

    private fun setRecyclerData() {
        for(i in arrayData) {
            getImageUser(i)
        }
    }

    private fun showRecyclist() {
        val listRequestAdapter = WaitingListAdapter(arrayRecycler)
        val apiClient = APIClient()

        Log.d(TAG, "fragment waiting list")
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listRequestAdapter

        listRequestAdapter.setOnAcceptClickCallback(object : WaitingListAdapter.OnAcceptClickCallback{
            override fun onAccpetClicked(data: FriendsRecyclerWaitingList) {
                data.data.id?.let {
                    apiClient.getApiService(konteks).acceptRequest(it.toString())
                        .enqueue(object: Callback<AcceptFriendResponse> {
                            override fun onResponse(
                                call: Call<AcceptFriendResponse>,
                                response: Response<AcceptFriendResponse>
                            ) {
                                if(response.code() == 200) {

                                }
                            }

                            override fun onFailure(call: Call<AcceptFriendResponse>, t: Throwable) {
                                Log.d(TAG, "$t")
                                _binding!!.swiperefreshWaitingList.isRefreshing = false
                            }

                        })
                }
            }
        })

        listRequestAdapter.setOnIgnoreClickCalllback(object: WaitingListAdapter.OnIgnoreClickCallback{
            override fun onIgnoreClicked(data: FriendsRecyclerWaitingList) {
                apiClient.getApiService(konteks).declineRequest(data.data.id.toString())
                    .enqueue(object: Callback<IgnoreFriendResponse> {
                        override fun onResponse(
                            call: Call<IgnoreFriendResponse>,
                            response: Response<IgnoreFriendResponse>
                        ) {
                            if(response.code() == 200) {
                                _binding!!.swiperefreshWaitingList.isRefreshing = false
                            }
                        }

                        override fun onFailure(call: Call<IgnoreFriendResponse>, t: Throwable) {
                            _binding!!.swiperefreshWaitingList.isRefreshing = false
                            Log.d(TAG, "$t")
                        }

                    })
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        konteks = context
    }

    override fun update() {
        refresh()
    }
}