package com.staydev.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.staydev.admin.R
import com.staydev.admin.model.Msewa
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterStatus (
    private val list: ArrayList<Msewa>,
    private val context: Context

) : RecyclerView.Adapter<AdapterStatus.ViewHolder>(){
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val mIdSewa: TextView = view.findViewById(R.id.idSewa)
        val mTanggal : TextView = view.findViewById(R.id.tanggalSewa)
        val mTotal : TextView = view.findViewById(R.id.totalSewa)
        val mDetail : ImageView = view.findViewById(R.id.detailSewa)

    }
    private lateinit var mDialogdetail:View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_status, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterStatus.ViewHolder, position: Int) {
        val  sewa = list[position]
        var total = sewa.total
        var tanggal = sewa.tgl_sewa



        val str: String = NumberFormat.getNumberInstance(Locale.US).format(total)
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val formattedDate = formatter.format(parser.parse(tanggal))

        holder.mIdSewa.text = sewa.id_sewa
        holder.mTanggal.text = formattedDate
        holder.mTotal.text = str



        holder.mDetail.setOnClickListener {
            Toast.makeText(context, "detail", Toast.LENGTH_SHORT).show()
            mDialogdetail = LayoutInflater.from(context).inflate(
                R.layout.activity_detail,
                null
            )
        }


    }
    override fun getItemCount(): Int = list.size
}