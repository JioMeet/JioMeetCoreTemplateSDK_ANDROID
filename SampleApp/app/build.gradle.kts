plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.jio.sdksampleapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.jio.sdksampleapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 2
        versionName = "2.0.1.7"
        vectorDrawables.useSupportLibrary = true
        setProperty("archivesBaseName", "JioMeetNativeSampleApp-${versionName}")
    }

    signingConfigs {
        register("release") {
            keyAlias = "com.jio.meet"
            keyPassword = "Jiomeet"
            storeFile = file("AndroidKeyStore.jks")
            storePassword = "Jiomeet"
        }

    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
            addAll(
                listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
                )
            )
        }
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/gradle/*")
        }
    }

}

repositories {
    maven {
        credentials {
            username = "user Name will be shared by JioMeet team"
            password = "github token will be shared by JioMeet"
        }
        url = uri("https://maven.pkg.github.com/JioMeet/JioMeetCoreTemplateSDK_ANDROID")
    }
    google()
    mavenCentral()
}



dependencies {
    implementation ("com.jiomeet.platform:jiomeetcoretemplatesdk:2.1.8")
    implementation ("androidx.core:core-ktx:1.9.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-android-compiler:2.44")
}
kapt {
    correctErrorTypes = true
}