package com.tsm.lemonade
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.lemonade.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    /**
     * JANGAN MENGUBAH VARIABEL ATAU NAMA NILAI ATAU NILAI AWAL MEREKA.
     *
     * Apa pun yang diberi label var alih-alih val diharapkan diubah dalam fungsinya tetapi JANGAN
     * ubah nilai awalnya yang dinyatakan di sini, ini dapat menyebabkan aplikasi tidak berfungsi dengan baik.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * Metode ini menyimpan status aplikasi jika diletakkan di latar belakang.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Mengklik akan menimbulkan respons yang berbeda tergantung pada negara bagian.
     * Metode ini menentukan status dan melanjutkan dengan tindakan yang benar.
     */
    private fun clickLemonImage() {

        when(lemonadeState){
            SELECT -> {
                lemonadeState = SQUEEZE
                lemonSize = lemonTree.pick()
                squeezeCount = 0
            }
            SQUEEZE -> {
                squeezeCount += 1
                lemonSize -= 1
                if (lemonSize==0){
                    lemonadeState = DRINK
                    lemonSize = 1
                }
            }
            DRINK -> {
                lemonadeState = RESTART
            }
            RESTART -> {
                lemonadeState = SELECT
            }
        }
        setViewElements()
    }

    /**
     * Mengatur elemen tampilan sesuai dengan status.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        when(lemonadeState){
            SELECT -> {
                textAction.setText(R.string.lemon_select)
                lemonImage?.setImageResource(R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                textAction.setText(R.string.lemon_squeeze)
                lemonImage?.setImageResource(R.drawable.lemon_squeeze)
            }
            DRINK -> {
                textAction.setText(R.string.lemon_drink)
                lemonImage?.setImageResource(R.drawable.lemon_drink)
            }
            RESTART -> {
                textAction.setText(R.string.lemon_empty_glass)
                lemonImage?.setImageResource(R.drawable.lemon_restart)
            }
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * Lama mengklik gambar lemon akan menunjukkan berapa kali lemon telah diperas.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * Kelas pohon lemon ini menggunakan metode memilih lemon. Dan ukuran lemonnya akan diacak
 * Dan menentukan berapa kali lemon perlu diperas sebelum Anda mendapatkan limun.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
