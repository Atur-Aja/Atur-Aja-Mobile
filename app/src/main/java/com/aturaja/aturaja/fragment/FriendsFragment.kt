package com.aturaja.aturaja.fragment

import android.content.Context
import android.content.Intent
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
import com.aturaja.aturaja.activity.ProfilFriendsActivity
import com.aturaja.aturaja.adapter.FriendsAdapter
import com.aturaja.aturaja.databinding.FragmentFriendsBinding
import com.aturaja.aturaja.model.Friends
import com.aturaja.aturaja.model.FriendsRecycler
import com.aturaja.aturaja.model.GetFriendResponse
import com.aturaja.aturaja.network.APIClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class FriendsFragment : Fragment(), Updateable {

    private lateinit var recyclerView: RecyclerView
    private lateinit var friendName : Array<String>
    private lateinit var friendEmail : Array<String>
    private lateinit var friendImage : Array<Int>
    private var arrayRecycler = ArrayList<FriendsRecycler>()
    private var _binding : FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private var friends = ArrayList<GetFriendResponse>()
    private lateinit var konteks: Context
    private val TAG = "FriendsFragment"

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
                        response.body()?.let {
                            friends.clear()
                            arrayRecycler.clear()
                            friends.addAll(it)
                            getDataRecycler()
                        }
                    }
                }

                override fun onFailure(call: Call<List<GetFriendResponse>>, t: Throwable) {
                    _binding!!.swiperefresh.isRefreshing = false
                    Log.d(TAG, "$t")
                }

            })
    }

    private fun getDataRecycler() {
        for (i in friends) {
            getImageUser(i)
        }
    }

    private fun getImageUser(data: GetFriendResponse) {
        val apiClient = APIClient()
        var bitmap: Bitmap

        apiClient.getApiService(konteks).getPhoto(data.photo.toString())
            .enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.code() == 200) {
                        bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                        val model = FriendsRecycler(data, bitmap)

                        arrayRecycler.add(model)
                        if(arrayRecycler.size == friends.size) {
                            showRecyclist()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(TAG, "Error : $t")
                }
            })
    }

    private fun showRecyclist() {
        val adapter = FriendsAdapter(arrayRecycler)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        adapter.setOnFriendsFragmentClickCallback(object: FriendsAdapter.OnFriendsFragmentClickCallback {
            override fun onClickItem(data: FriendsRecycler) {
                val intent = Intent(konteks, ProfilFriendsActivity::class.java)
                intent.putExtra("username", data.friends.username)

                startActivity(intent)
            }

        })
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