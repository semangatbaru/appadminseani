package com.staydev.admin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.staydev.admin.helper.SharedPrefManager
import com.staydev.admin.model.Mlogin
import com.staydev.admin.helper.Urls
import com.staydev.admin.helper.VolleySingleton
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin:CardView
    private lateinit var inputEmail:EditText
    private lateinit var inputPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        if (SharedPrefManager.getInstance(this).isLoggedIn) {
//            finish()
//            startActivity(Intent(this, Dashboard::class.java))
//        }

        btnLogin = findViewById(R.id.btnLogin)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)

        btnLogin.setOnClickListener {
            regis()
        }
    }

    private fun regis() {
        //first getting the values

        //base()
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()
        //validating inputs
        if (TextUtils.isEmpty(email)) {
            inputEmail.error = "Masukkan Email Anda"
            inputEmail.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.error = "Masukkan Password"
            inputPassword.requestFocus()
            return
        }


        //if everything is fine

        val stringRequest = object : StringRequest(
                Method.POST, Urls.URL_Admin,
                Response.Listener { response ->
                    Log.d("response", response)


                    try {
                        //converting response to json object
                        val obj = JSONObject(response)

                        //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()

                        //if no error in response
                        if (obj.getString("success") == "true") {
                            //Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()

                            //getting the user from the response
                            val userJson = obj.getJSONObject("data")

                            //creating a new user object

                            val mUser = Mlogin(
                                    userJson.getInt("id_admin"),
                                    userJson.getString("email"),
                                    userJson.getString("token")
                            )
                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(applicationContext).mUserLogin(mUser)
                            //starting the MainActivity

                            finish()
                            startActivity(Intent(applicationContext, Dashboard::class.java))
                            Toast.makeText(applicationContext,obj.getString("message"),Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(
                                    this,
                                    obj.getString("message"),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): LinkedHashMap<String, String> {
                val params = LinkedHashMap<String, String>()
                params["email"] = email;
                params["password"] = password



                Log.e("param", params.toString())
                return params
            }
//            override fun getHeaders(): Map<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params["Content-Type"] = "application/json"
//                return params
//            }
        }
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }
}