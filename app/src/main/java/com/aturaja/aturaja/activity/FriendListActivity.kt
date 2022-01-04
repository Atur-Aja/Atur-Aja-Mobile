package com.aturaja.aturaja.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.aturaja.aturaja.R
import com.aturaja.aturaja.adapter.FriendListPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FriendListActivity : AppCompatActivity() {
    var tabTitle = arrayOf("Friends", "Waiting List")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        var viewPager = findViewById<ViewPager2>(R.id.viewPagerFriends)
        var tabLayout = findViewById<TabLayout>(R.id.tabLayoutFriends)
        viewPager.adapter = FriendListPagerAdapter(supportFragmentManager, lifecycle)


        TabLayoutMediator(tabLayout,viewPager){
                tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    fun addFriendOnClick(view: android.view.View) {
        val intent = Intent(this, AddFriendActivity::class.java)
        startActivity(intent)
    }

    fun backOnClick(view: android.view.View) {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}