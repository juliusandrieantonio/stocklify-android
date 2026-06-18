package com.scalo.stocklify.core.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = Constants.PREFS_NAME)