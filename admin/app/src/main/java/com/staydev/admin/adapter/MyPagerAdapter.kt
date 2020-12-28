import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.staydev.admin.JadwalUlangFragment
import com.staydev.admin.ProsesFragment
import com.staydev.admin.SetujuFragment

class MyPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        ProsesFragment(),
        JadwalUlangFragment(),
        SetujuFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Terbaru"
            1 -> "Jadwal Ulang"
            else -> "Setuju"
        }
    }
}