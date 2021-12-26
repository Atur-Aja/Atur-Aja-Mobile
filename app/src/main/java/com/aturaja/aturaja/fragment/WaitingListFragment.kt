package com.aturaja.aturaja.fragment

import android.content.Context
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
import com.aturaja.aturaja.model.AcceptFriendResponse
import com.aturaja.aturaja.model.GetAllFriendRequestResponse
import com.aturaja.aturaja.model.IgnoreFriendResponse
import com.aturaja.aturaja.model.WaitingList
import com.aturaja.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WaitingListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaitingListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<WaitingList>
    private lateinit var friendName : Array<String>
    private lateinit var friendEmail : Array<String>
    private lateinit var friendImage : Array<Int>
    private var arrayData = ArrayList<GetAllFriendRequestResponse>()
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

        friendName = arrayOf(
            "Jack Sparrow",
            "Jack Sparrow",
            "Jack Sparrow"

        )

        friendEmail = arrayOf(
            "pirates@gmail.com",
            "pirates@gmail.com",
            "pirates@gmail.com"

        )

        friendImage = arrayOf(
            R.drawable.a,
        )


        recyclerView = binding.waitingListRecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.setHasFixedSize(true)

//        arrayList = arrayListOf()
//        getUserData()
        Log.d(TAG, "view created waiting list")
        refresh()
        getData()
//        showRecyclist()
    }

    private fun getUserData() {
        for(i in friendImage.indices){

            val waitingList = WaitingList(friendImage[i],friendName[i],friendEmail[i])
            arrayList.add(waitingList)

        }

//        recyclerView.adapter = WaitingListAdapter(arrayList)
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
                        showRecyclist(response.body())
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

    private fun showRecyclist(code: List<GetAllFriendRequestResponse>?) {
        val listRequestAdapter = WaitingListAdapter(code as ArrayList<GetAllFriendRequestResponse>)
        val apiClient = APIClient()

        Log.d(TAG, "fragment waiting list")
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = listRequestAdapter

        listRequestAdapter.setOnAcceptClickCallback(object : WaitingListAdapter.OnAcceptClickCallback{
            override fun onAccpetClicked(data: GetAllFriendRequestResponse) {
                data.id?.let {
                    apiClient.getApiService(konteks).acceptRequest(it.toString())
                        .enqueue(object: Callback<AcceptFriendResponse> {
                            override fun onResponse(
                                call: Call<AcceptFriendResponse>,
                                response: Response<AcceptFriendResponse>
                            ) {
                                if(response.code() == 200) {
                                    _binding!!.swiperefreshWaitingList.isRefreshing = false
                                    getData()
                                }
                            }

                            override fun onFailure(call: Call<AcceptFriendResponse>, t: Throwable) {
                                Log.d(TAG, "$t")
                            }

                        })
                }
            }
        })

        listRequestAdapter.setOnIgnoreClickCalllback(object: WaitingListAdapter.OnIgnoreClickCallback{
            override fun onIgnoreClicked(data: GetAllFriendRequestResponse) {
                apiClient.getApiService(konteks).declineRequest(data.id.toString())
                    .enqueue(object: Callback<IgnoreFriendResponse> {
                        override fun onResponse(
                            call: Call<IgnoreFriendResponse>,
                            response: Response<IgnoreFriendResponse>
                        ) {
                            if(response.code() == 200) {
                                _binding!!.swiperefreshWaitingList.isRefreshing = false
                                getData()
                            }
                        }

                        override fun onFailure(call: Call<IgnoreFriendResponse>, t: Throwable) {
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
}