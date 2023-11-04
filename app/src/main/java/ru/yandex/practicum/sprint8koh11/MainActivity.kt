package ru.yandex.practicum.sprint8koh11

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.core.content.contentValuesOf
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private var textView: TextView? = null
    private var timer: Timer? = null
    private var lastOnStopTimeStamp: Long = 0

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("SPRINT_8", "$this onCreate")

        val isTablet = resources.getBoolean(R.bool.is_tablet)

        counter = savedInstanceState?.getInt("counter", 0) ?: 0
        textView = findViewById<TextView>(R.id.asd)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("counter", counter)

        outState.putParcelable("my_data", MyData())

    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onStart() {
        super.onStart()
        Log.d("SPRINT_8", "$this onStart")
        if (lastOnStopTimeStamp != 0L) {
            Log.d(
                "SPRINT_8",
                "time since last onStop ${System.currentTimeMillis() - lastOnStopTimeStamp}"
            )
        }
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                counter++
                textView?.post {
                    textView?.setText(counter.toString())
                }
            }

        }, 0, 1000L)
    }

    override fun onResume() {
        super.onResume()
        Log.d("SPRINT_8", "$this onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SPRINT_8", "$this onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SPRINT_8", "$this onStop System.currentTimeMillis()=${System.currentTimeMillis()}")
        lastOnStopTimeStamp = System.currentTimeMillis() // 1698946808545

        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SPRINT_8", "$this onDestroy ${isFinishing}")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("SPRINT_8", "$this onConfigurationChanged $newConfig")

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}


class MyData() : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyData> {
        override fun createFromParcel(parcel: Parcel): MyData {
            return MyData(parcel)
        }

        override fun newArray(size: Int): Array<MyData?> {
            return arrayOfNulls(size)
        }
    }

}