package com.staydev.admin.ui.home

import MyPagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.material.tabs.TabLayout
import com.staydev.admin.R
import com.staydev.admin.adapter.AdapterAgenda
import com.staydev.admin.adapter.AdapterStatus
import com.staydev.admin.helper.SharedPrefManager
import com.staydev.admin.helper.Urls
import com.staydev.admin.helper.VolleySingleton
import com.staydev.admin.model.Msewa
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var root: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var list: ArrayList<Msewa>
    private lateinit var mSwipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)

        list = ArrayList()

        mRecyclerView = root.findViewById(R.id.mRecyclerViewAgenda)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mSwipeRefresh = root.findViewById(R.id.swipeRefreshAgenda)
        mSwipeRefresh.setOnRefreshListener {
            getData()
            mSwipeRefresh.isRefreshing = false

        }

        getData()


        return root
    }
    private fun getData() {
        val user = SharedPrefManager.getInstance(requireContext()).mloginadmin
        val token = user.token.toString()
        Log.d("token", token)


        val stringRequest = object : StringRequest(
                Method.GET, Urls.URL_Agenda,
                Response.Listener { response ->
                    Log.d("respons", response);

                    try {
                        //converting response to json object
                        val obj = JSONObject(response)


                        //Toast.makeText(activity, obj.getString("message"), Toast.LENGTH_LONG).show()

                        //if no error in response
                        if (obj.getString("success") == "true") {
                            list.clear()

                            Log.i("data", obj.getString("data"));
                            val dataJson = obj.getJSONArray("data")

                            for (i in 0 until dataJson.length()) {
                                val data = dataJson.getJSONObject(i)
                                list.add(
                                        Msewa(
                                                data.getString("id_sewa"),
                                                data.getInt("id_user"),
                                                data.getString("tgl_sewa"),
                                                data.getInt("total"),
                                                data.getString("status"),
                                                data.getDouble("lahan")
                                        )
                                )
                            }
                            mRecyclerView.adapter = AdapterAgenda(list, requireContext())

                        } else {
                            Toast.makeText(activity, obj.getString("message"), Toast.LENGTH_SHORT)
                                    .show()

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(
                            activity,
                            "load",
                            Toast.LENGTH_SHORT
                    ).show()
                    Log.d("eror", error.message.toString())
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()
                return params
            }
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }


        VolleySingleton.getInstance(requireContext()).addToRequestQueue(stringRequest)
    }
}