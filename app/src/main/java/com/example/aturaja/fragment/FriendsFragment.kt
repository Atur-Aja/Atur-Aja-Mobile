package com.example.aturaja.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aturaja.R
import com.example.aturaja.adapter.FriendsAdapter
import com.example.aturaja.databinding.FragmentFriendsBinding
import com.example.aturaja.model.Friends
import com.example.aturaja.model.GetFriendResponse
import com.example.aturaja.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment(), Updateable {

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<Friends>
    private lateinit var friendName : Array<String>
    private lateinit var friendEmail : Array<String>
    private lateinit var friendImage : Array<Int>
    private var _binding : FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var konteks: Context
    private val TAG = "FriendsFragment"
    private var newArrayList = ArrayList<GetFriendResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.friendRecyclerView

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
            R.drawable.a
        )

        getData()
        refresh()
    }

    fun refresh() {
        _binding?.swiperefresh?.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        val apiClient = APIClient()

        apiClient.getApiService(konteks).getFriends()
            .enqueue(object: Callback<List<GetFriendResponse>> {
                override fun onResponse(
                    call: Call<List<GetFriendResponse>>,
                    response: Response<List<GetFriendResponse>>
                ) {
                    if(response.code() == 200) {
                        _binding!!.swiperefresh.isRefreshing = false
                        showRecyclist(response.body())
                    }
                }

                override fun onFailure(call: Call<List<GetFriendResponse>>, t: Throwable) {
                    _binding!!.swiperefresh.isRefreshing = false
                    Log.d(TAG, "$t")
                }

            })
    }

    private fun showRecyclist(body: List<GetFriendResponse>?) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = FriendsAdapter(body as ArrayList<GetFriendResponse>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        konteks = context
        Log.d(TAG, "fragmet friend attach")
    }

    override fun update() {
        refresh()
    }
}

interface Updateable {
    fun update()
}