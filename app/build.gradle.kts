plugins {
    id("com.android.application")
    id("com.google.gms.google-services") //new
}


android {
    namespace = "com.example.yusefcapstione"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.yusefcapstione"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.0")) //new


    implementation("com.google.firebase:firebase-analytics") //new
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")
    implementation("com.jjoe64:graphview:4.2.2")//GraphView
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  //  implementation ("androidx.camera:camera-view:1.3.1.0-alpha03.0-alpha30") //just incase,may not need
    implementation ("androidx.camera:camera-camera2:1.3.1") //camera
    implementation ("androidx.camera:camera-lifecycle:1.3.1") //camera
    implementation ("androidx.camera:camera-view:1.3.1") //camera
    implementation ("androidx.camera:camera-core:1.3.1") //camera
}


