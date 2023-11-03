package ru.yandex.practicum.sprint8koh11

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.LabeledIntent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import ru.yandex.practicum.sprint8koh11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appSettings.setOnClickListener {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
            )
        }
        binding.relaunch.setOnClickListener {
            finish()
            startActivity(
                packageManager.getLaunchIntentForPackage(packageName)
            )
        }
        binding.chooser.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
            }
            startActivity(Intent.createChooser(sendIntent, "Объяснение зачем это нужно"))
        }
        binding.notification.setOnClickListener {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


            //for Android 5-7
            //intent.putExtra("app_package", packageName)
            //intent.putExtra("app_uid", applicationInfo.uid)

            // for Android 8 and above
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)

            startActivity(intent)
        }

        binding.skype.setOnClickListener {
            val skypeIntent = packageManager.getLaunchIntentForPackage("com.skype.raider")
            if (skypeIntent != null) {
                startActivity(skypeIntent)
            } else {
                try {
                    startActivity(Intent(Intent.ACTION_SEND))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        this@MainActivity,
                        "ActivityNotFoundException",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.filter.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
            }
            val resInfoList = packageManager.queryIntentActivities(shareIntent, 0)
            val extraIntents = resInfoList
                .filter {
                    it.activityInfo.packageName.startsWith("com.google.android.apps")
                }
                .map { resolveInfo ->
                    val intent = Intent()
                    intent.component = ComponentName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name
                    )
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.`package` = resolveInfo.activityInfo.packageName
                    LabeledIntent(
                        intent,
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.labelRes,
                        resolveInfo.icon
                    )
                }.toMutableList()

            val chooser = Intent.createChooser(extraIntents.removeAt(0), "My share")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toTypedArray())
            startActivity(chooser)
        }
    }
}