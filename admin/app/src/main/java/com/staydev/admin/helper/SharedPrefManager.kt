package com.staydev.admin.helper

import android.content.Context
import android.content.Intent
import com.staydev.admin.LoginActivity
import com.staydev.admin.model.Mlogin

class SharedPrefManager private constructor(context: Context){
    //this method will checker whether user is already logged in or not
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences?.getString(KEY_EMAIL, null) != null
        }

    //this method will give the logged in user
    val mloginadmin: Mlogin
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return Mlogin(
                    sharedPreferences!!.getInt(KEY_IDADMIN, 0),
                    sharedPreferences.getString(KEY_EMAIL, null),
                    sharedPreferences.getString(KEY_TOKEN, null)

            )
        }


    init {
        ctx = context
    }

    //this method will store the pegawai data in shared preferences
    fun mUserLogin(mlogin: Mlogin) {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putInt(KEY_IDADMIN, mlogin.id_admin)
        editor?.putString(KEY_EMAIL, mlogin.email)
        editor?.putString(KEY_TOKEN, mlogin.token)
        editor?.apply()
    }

    //this method will logout the user
    fun logout() {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
        ctx?.startActivity(Intent(ctx, LoginActivity::class.java))
    }

    companion object {

        private val SHARED_PREF_NAME = "login"
        private const val KEY_IDADMIN = "keyidmerchant"
        private val KEY_EMAIL = "keyemail"
        private val KEY_TOKEN = "keytoken"
        private var mInstance: SharedPrefManager? = null
        private var ctx: Context? = null
        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance as SharedPrefManager
        }
    }
}