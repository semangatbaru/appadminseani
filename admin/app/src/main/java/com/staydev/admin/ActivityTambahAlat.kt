package com.staydev.admin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.staydev.admin.helper.Urls
import com.staydev.admin.helper.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class ActivityTambahAlat: AppCompatActivity() {

    private var imageData: ByteArray? = null
    companion object { private const val IMAGE_PICK_CODE = 999 }
    private lateinit var bitmap: Bitmap
    private lateinit var encode: String
    private lateinit var sss: String

    private lateinit var mFoto: ImageView
    private lateinit var mBack: ImageView
    private lateinit var mNamaAlat: EditText
    private lateinit var mStokAlat: EditText
    private lateinit var mHargaAlat: EditText
    private lateinit var mDeskripsiAlat: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alat)

        mBack = findViewById(R.id.back)
        mBack.setOnClickListener {
            onBackPressed()
        }
        mNamaAlat = findViewById(R.id.inAlat)
        mHargaAlat =findViewById(R.id.inHarga)
        mStokAlat = findViewById(R.id.inStok)
        mDeskripsiAlat = findViewById(R.id.inDeskripsi)
        btnSave = findViewById(R.id.save)


        mFoto = findViewById(R.id.inFoto)

        mFoto.setOnClickListener {
            launchGallery()

        }
        btnSave.setOnClickListener {
            saveAlat()
        }
    }
    private fun saveAlat() {

        //first getting the values
        val nama_alat = mNamaAlat.text.toString()
        val harga = mHargaAlat.text.toString()
        val stok = mStokAlat.text.toString()
        val deskripsi = mDeskripsiAlat.text.toString()
        val foto = "data:image/jpeg;base64,$encode"


        //validating inputs
        if (TextUtils.isEmpty(nama_alat)) {
            mNamaAlat.error = "Tidak boleh kosong"
            mNamaAlat.requestFocus()
            return
        }
        if (TextUtils.isEmpty(harga)) {
            mHargaAlat.error = "Tidak boleh kosong"
            mHargaAlat.requestFocus()
            return
        }
        if (TextUtils.isEmpty(stok)) {
            mStokAlat.error = "Tidak boleh kosong"
            mStokAlat.requestFocus()
            return
        }
        if (TextUtils.isEmpty(deskripsi)) {
            mDeskripsiAlat.error = "Tidak boleh kosong"
            mDeskripsiAlat.requestFocus()
            return
        }
        Log.e("prrrr", nama_alat+harga+stok+deskripsi)
//        if (TextUtils.isEmpty(foto)) {
//            mFoto. = "Tidak boleh kosong"
//            mFoto.requestFocus()
//            return
//        }




        //if everything is fine



        val stringRequest = object : StringRequest(
                Method.POST, Urls.URL_Alat,
                Response.Listener { response ->
                    Log.d("respons", response);


                    try {
                        //converting response to json object
                        val obj = JSONObject(response)


                        //Toast.makeText(activity, obj.getString("message"), Toast.LENGTH_LONG).show()

                        //if no error in response
                        if (obj.getString("success") == "true") {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_LONG).show();
                            onBackPressed()
                        } else {
                            Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT)
                                    .show()

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(
                            this,
                            error.message,
                            Toast.LENGTH_SHORT
                    ).show()
                    Log.d("eror", error.message.toString())
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()
                params["nama_alat"] = nama_alat
                params["harga"] = harga
                params["stok"] = stok
                params["foto"] = foto
                params["deskripsi"] = deskripsi
                Log.e("params", params.toString())
                return params
            }
        }


        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = this.contentResolver?.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                mFoto.setImageURI(uri)
                createImageData(uri)
                val drawable: BitmapDrawable = mFoto.drawable as BitmapDrawable
                bitmap = drawable.bitmap
                //tahu = encodeTobase64(bitmap)


                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                encode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.d("encode", encode )
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}