package com.staydev.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.staydev.admin.adapter.AdapterStatus
import com.staydev.admin.helper.SharedPrefManager
import com.staydev.admin.helper.Urls
import com.staydev.admin.model.Msewa

import com.staydev.admin.helper.VolleySingleton
import org.json.JSONException
import org.json.JSONObject

class JadwalUlangFragment : Fragment() {

    private lateinit var root:View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var list: ArrayList<Msewa>
    private lateinit var mSwipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_jadwalulang, container, false)

        list = ArrayList()

        mRecyclerView = root.findViewById(R.id.mRecyclerViewJU)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mSwipeRefresh = root.findViewById(R.id.swipeRefreshProses)
        mSwipeRefresh.setOnRefreshListener {
            getStatusJU()
            mSwipeRefresh.isRefreshing = false

        }

        getStatusJU()
        return root
    }

    private fun getStatusJU() {
        val user = SharedPrefManager.getInstance(requireContext()).mloginadmin
        val token = user.token.toString()
        Log.d("token", token)


        val stringRequest = object : StringRequest(
            Method.GET, Urls.URL_JadwalUlang,
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
                        mRecyclerView.adapter = AdapterStatus(list, requireContext())

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
                    activity,"server sibuk, sedang memuat",
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