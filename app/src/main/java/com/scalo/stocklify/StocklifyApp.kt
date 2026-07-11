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

    // Injected as dagger.Lazy so construction (and the passphrase lookup it triggers)
    // doesn't happen on the main thread during Hilt's Application field injection.
    @Inject
    lateinit var appDatabase: Lazy<AppDatabase>

    override fun onCreate() {
        super.onCreate()
        OpenCVLoader.initLocal()

        // Warm up the encrypted database off the main thread. Without this, the first
        // screen to touch Room (e.g. Create Store right after "Continue as Guest") can
        // ANR: DatabaseModule.provideDatabase() runBlocking-s the passphrase lookup, and
        // if that's the very first access it can run long enough on the main thread to
        // trip the watchdog.
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.get()
        }
    }
}