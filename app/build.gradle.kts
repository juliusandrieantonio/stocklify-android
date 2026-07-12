import com.android.build.api.dsl.ApplicationProductFlavor
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.scalo.stocklify"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.scalo.stocklify"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters += "arm64-v8a"
        }
    }

    flavorDimensions += "env"

    productFlavors {
        // TODO: CREATE DIFFERENT SUPABASE ENV
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Stocklify Dev")
            buildConfigField("String", "SUPABASE_URL", "\"https://elwlpmvkryindzbkmfrv.supabase.co\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"sb_publishable_E8XV8GhasVVHGyvuTdw1YQ_PctHuvXy\"")
        }
        create("qa") {
            dimension = "env"
            applicationIdSuffix = ".qa"
            versionNameSuffix = "-qa"
            buildConfigField("String", "SUPABASE_URL", "\"https://elwlpmvkryindzbkmfrv.supabase.co\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"sb_publishable_E8XV8GhasVVHGyvuTdw1YQ_PctHuvXy\"")
        }
        create("uat") {
            dimension = "env"
            applicationIdSuffix = ".uat"
            versionNameSuffix = "-uat"
            buildConfigField("String", "SUPABASE_URL", "\"https://elwlpmvkryindzbkmfrv.supabase.co\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"sb_publishable_E8XV8GhasVVHGyvuTdw1YQ_PctHuvXy\"")
        }
        create("prod") {
            dimension = "env"
            resValue("string", "app_name", "Stocklify")
            buildConfigField("String", "SUPABASE_URL", "\"https://elwlpmvkryindzbkmfrv.supabase.co\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"sb_publishable_E8XV8GhasVVHGyvuTdw1YQ_PctHuvXy\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        jniLibs {
            pickFirsts += setOf(
                "lib/arm64-v8a/libc++_shared.so",
                "lib/armeabi-v7a/libc++_shared.so"
            )
        }
    }
}

dependencies {

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.material)

    // Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // Preferences
    implementation(libs.datastore.preferences)

    // PaddleOCR
    implementation(libs.paddleocr)

    // OpenCV
    implementation(libs.opencv)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.sqlcipher)
    implementation(libs.sqlite)

    // Supabase
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    implementation(libs.supabase.storage)
    implementation(libs.supabase.realtime)
    implementation(libs.supabase.functions)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.serialization.json)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

}