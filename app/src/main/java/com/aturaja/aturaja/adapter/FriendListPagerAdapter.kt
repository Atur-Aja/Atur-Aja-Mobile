package com.aturaja.aturaja.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aturaja.aturaja.fragment.FriendsFragment
import com.aturaja.aturaja.fragment.WaitingListFragment

class FriendListPagerAdapter(fragmentManager : FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return FriendsFragment()
            1 -> return WaitingListFragment()
            else -> return FriendsFragment()
        }
    }
}