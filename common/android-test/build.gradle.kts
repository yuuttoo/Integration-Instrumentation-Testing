import com.ivy.buildsrc.*

plugins {
    `android-library`
    `kotlin-android`

    id("de.mannodermaus.android-junit5") version "1.9.3.0"

}

apply<com.ivy.buildsrc.IvyPlugin>()

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:ui"))
    Hilt()
    HiltTesting(
        dependency = { api(it) },
        kaptProcessor = { kapt(it) }
    )
    RoomDB(false)
    implementation(project(":common:main"))
    implementation(project(":core:domain"))
    implementation(project(":core:persistence"))
    implementation("app.cash.turbine:turbine:${Versions.turbine}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}")


    Kotlin(api = false)
    Coroutines(api = false)
    AndroidXTest(dependency = { api(it) })


    Testing(
        // :common:test needs to be added as implementation dep
        // else won't work
        commonTest = false,
        // Prevent circular dependency
        commonAndroidTest = false
    )
    api(project(":common:test")) // expose :common:test classes to all androidTest
}
android {
    namespace = "com.ivy.common.androidtest"
}
