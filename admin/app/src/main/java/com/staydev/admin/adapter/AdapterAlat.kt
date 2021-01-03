package com.staydev.admin.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.squareup.picasso.Picasso
import com.staydev.admin.R
import com.staydev.admin.helper.SharedPrefManager
import com.staydev.admin.helper.Urls
import com.staydev.admin.helper.VolleySingleton
import com.staydev.admin.model.Malat
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterAlat(
        private val list: ArrayList<Malat>,
        private val context: Context
) : RecyclerView.Adapter<AdapterAlat.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val mNama: TextView = view.findViewById(R.id.namaItem)
        val mHarga : TextView = view.findViewById(R.id.hargaItem)
        val mEdit : ImageView = view.findViewById(R.id.tombol_edit_item)
        val mHapus : ImageView = view.findViewById(R.id.tombol_hapus_item)
        val mGambar : ImageView = view.findViewById(R.id.gambarItem)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterAlat.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_alat, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterAlat.ViewHolder, position: Int) {
        val  alat = list[position]
        var harga = alat.harga
        val id_alat = alat.id_alat
        var url = alat.foto



        val str: String = NumberFormat.getNumberInstance(Locale.US).format(harga)
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd MMM yyyy")

        Picasso.get().load(url).into(holder.mGambar)


        holder.mNama.text = alat.nama_alat
        holder.mHarga.text = str

        holder.mEdit.setOnClickListener {
            //Toast.makeText(context, "edit$id_alat", Toast.LENGTH_SHORT).show()
            Log.e("edit", id_alat.toString())
        }
        holder.mHapus.setOnClickListener {
            Toast.makeText(context, "hapus$id_alat", Toast.LENGTH_SHORT).show()
            hapus(id_alat)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun hapus(id_alat:Int) {


        //if everything is fine

        val stringRequest = object : StringRequest(
                Method.PUT, Urls.URL_Hapus + "/"+ id_alat,
                Response.Listener { response ->
                    Log.d("response", response)


                    try {
                        //converting response to json object
                        val obj = JSONObject(response)

                        //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()

                        //if no error in response
                        if (obj.getString("success") == "true") {
                            //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                            getData()
                        } else {
                            Toast.makeText(
                                    context,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                Log.e("param", params.toString())
                return params
            }
        }
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
    }
    private fun getData() {



        val stringRequest = object : StringRequest(
                Method.GET, Urls.URL_Alat,
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
                                        Malat(

                                                data.getInt("id_alat"),
                                                data.getString("nama_alat"),
                                                data.getInt("stok"),
                                                data.getInt("harga"),
                                                data.getString("foto"),
                                                data.getString("deskripsi")
                                        )
                                )
                            }
                            notifyDataSetChanged()

                        } else {
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT)
                                    .show()

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(
                            context,
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

        }


        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
    }


}