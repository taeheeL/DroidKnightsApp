import com.droidknights.app2023.configureHiltAndroid
import com.droidknights.app2023.libs

plugins {
    id("droidknights.android.library")
    id("droidknights.android.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:navigation"))

    val libs = project.extensions.libs
    implementation(libs.findLibrary("hilt.navigation.compose").get())
    implementation(libs.findLibrary("androidx.compose.navigation").get())
    androidTestImplementation(libs.findLibrary("androidx.compose.navigation.test").get())

    testImplementation(libs.findLibrary("junit4").get())
    testImplementation(libs.findLibrary("mockk").get())
}
