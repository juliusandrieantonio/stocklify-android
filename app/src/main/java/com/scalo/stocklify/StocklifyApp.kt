package com.scalo.stocklify

import android.app.Application
import com.scalo.stocklify.core.data.database.AppDatabase
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.opencv.android.OpenCVLoader
import javax.inject.Inject

@HiltAndroidApp
class StocklifyApp : Application() {

    @Inject
    lateinit var appDatabase: Lazy<AppDatabase>

    override fun onCreate() {
        super.onCreate()
        OpenCVLoader.initLocal()

        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.get()
        }
    }
}