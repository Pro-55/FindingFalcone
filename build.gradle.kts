// https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application") version libs.versions.androidGradlePlugin.get() apply false
    kotlin("android") version libs.versions.kotlin.get() apply false
    kotlin("plugin.serialization") version libs.versions.kotlin.get() apply false
    id("com.google.devtools.ksp") version libs.versions.ksp.get() apply false
    id("com.google.dagger.hilt.android") version libs.versions.hilt.get() apply false
    id("androidx.navigation.safeargs") version libs.versions.navigationSafeArgs apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}