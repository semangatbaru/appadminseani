package com.staydev.admin.ui.home

import MyPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.staydev.admin.R

class HomeFragment : Fragment() {

    private lateinit var root: View
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabs: TabLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)

        mViewPager = root.findViewById(R.id.viewpager_main)
        mTabs = root.findViewById(R.id.tabs_main)

        mViewPager.adapter = MyPagerAdapter(requireFragmentManager())
        mTabs.setupWithViewPager(mViewPager)

        return root
    }
}