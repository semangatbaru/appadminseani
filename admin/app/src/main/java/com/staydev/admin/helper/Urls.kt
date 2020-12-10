package com.staydev.seani.helper

object Urls {
        private val ROOT_URL = "http://192.168.11.160:8000/api/"
//   private val ROOT_URL = "http://192.168.43.155:8000/api/"

    val URL_Register = ROOT_URL + "register"
    val URL_Alat = ROOT_URL + "alat"
    val URL_Pelanggan = ROOT_URL + "pelanggan"
    val URL_GetPegawai = ROOT_URL + "pegawai"
    val URL_Merchant = ROOT_URL + "merchant"
    val URL_Kategori = ROOT_URL + "kategori"

    val URL_Upload = ROOT_URL + "upload"
//    val URL_Gambar = "http://192.168.43.155:8000/storage/itemImg/"
    val URL_Gambar = "http://192.168.11.160:8000/storage/itemImg/"

    val JSON_ARRAY = "values"

}
