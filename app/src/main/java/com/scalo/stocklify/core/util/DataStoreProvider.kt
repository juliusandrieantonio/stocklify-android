package com.scalo.stocklify.core.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.scalo.stocklify.core.util.constants.ConstantPreferences

val Context.dataStore by preferencesDataStore(name = ConstantPreferences.PREFS_NAME)