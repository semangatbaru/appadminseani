package com.staydev.admin

import MyPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class Dashboard : AppCompatActivity() {
    private lateinit var mViewPager:ViewPager
    private lateinit var mTabs:TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mViewPager = findViewById(R.id.viewpager_main)
        mTabs = findViewById(R.id.tabs_main)

        mViewPager.adapter = MyPagerAdapter(supportFragmentManager)
        mTabs.setupWithViewPager(mViewPager)
    }
}