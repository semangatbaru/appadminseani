package com.staydev.admin.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.staydev.admin.R
import com.staydev.admin.helper.SharedPrefManager
import com.staydev.admin.helper.Urls
import com.staydev.admin.helper.VolleySingleton
import com.staydev.admin.model.Msewa
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterProses (
    private val list: ArrayList<Msewa>,
    private val context: Context

) : RecyclerView.Adapter<AdapterProses.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val mIdSewa: TextView = view.findViewById(R.id.idSewa)
        val mTanggal : TextView = view.findViewById(R.id.tanggalSewa)
        val mTotal : TextView = view.findViewById(R.id.totalSewa)
        val mEdit : ImageView = view.findViewById(R.id.modalEdit)
//        val mClose:ImageView = view.findViewById(R.id.modalClose)
//        val meditBtn:ImageView = view.findViewById(R.id.btn_edit)
        val mSetuju : ImageView = view.findViewById(R.id.setuju)

    }
    private lateinit var mDialogViewEdit: View
    private lateinit var mClose:ImageView
    private lateinit var mTanggal:CalendarView
    private lateinit var mBtnEdit:ImageButton


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_proses, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterProses.ViewHolder, position: Int) {
        val  sewa = list[position]
        var total = sewa.total
        var tanggal = sewa.tgl_sewa
        var id_sewa = sewa.id_sewa



        val str: String = NumberFormat.getNumberInstance(Locale.US).format(total)
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val formattedDate = formatter.format(parser.parse(tanggal))

        holder.mIdSewa.text = sewa.id_sewa
        holder.mTanggal.text = formattedDate
        holder.mTotal.text = str



        holder.mEdit.setOnClickListener {
             mDialogViewEdit = LayoutInflater.from(context).inflate(
                R.layout.activity_edit_sewa,
                null
            )
            mClose = mDialogViewEdit.findViewById(R.id.modalClose)
            mBtnEdit = mDialogViewEdit.findViewById(R.id.btn_edit)
            mTanggal = mDialogViewEdit.findViewById(R.id.tanggalEdit)

            val mBuilder = AlertDialog.Builder(context)
                    .setView(mDialogViewEdit)
            //show dialog
            val mAlertDialog = mBuilder.show()
            mBtnEdit.setOnClickListener {
                //dismiss dialog
                //get text from EditTexts of custom layout

                ubah(mAlertDialog, id_sewa)
                mAlertDialog.dismiss()

            }
            //cancel button click of custom layout
            mClose.setOnClickListener {
                mAlertDialog.dismiss()
            }

        }
        holder.mSetuju.setOnClickListener {
            setuju(id_sewa, tanggal)
        }


    }
    override fun getItemCount(): Int = list.size

    private fun setuju(id_sewa:String, tanggal:String) {



        //if everything is fine

        val stringRequest = object : StringRequest(
                Method.PUT, Urls.URL_Edit + "/"+ id_sewa,
                Response.Listener { response ->
                    Log.d("response", response)


                    try {
                        //converting response to json object
                        val obj = JSONObject(response)

                        //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()

                        //if no error in response
                        if (obj.getString("success") == "true") {
                            Toast.makeText(context, "di setujui", Toast.LENGTH_LONG).show()
                            getStatusProses()
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
                Response.ErrorListener { error -> Toast.makeText(context, "server sibuk, ulangi", Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["tgl_sewa"] = tanggal
                params["status"] = "disetujui"

                Log.e("param", params.toString())
                return params
            }
        }
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
    }
    private fun ubah(mDialog: AlertDialog, id_sewa:String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val tgl_sewa = sdf.format(Date(mTanggal.date))


        //if everything is fine

        val stringRequest = object : StringRequest(
                Method.PUT, Urls.URL_Edit + "/"+ id_sewa,
                Response.Listener { response ->
                    Log.d("response", response)


                    try {
                        //converting response to json object
                        val obj = JSONObject(response)

                        //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()

                        //if no error in response
                        if (obj.getString("success") == "true") {
                            Toast.makeText(context, "di Jadwal Ulang", Toast.LENGTH_LONG).show()
                            getStatusProses()
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
                Response.ErrorListener { error -> Toast.makeText(context, "server sibuk, ulangi", Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["tgl_sewa"] = tgl_sewa
                params["status"] = "ju"

                Log.e("param", params.toString())
                return params
            }
        }
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
    }
    private fun getStatusProses() {


        val stringRequest = object : StringRequest(
                Method.GET, Urls.URL_Terbaru,
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