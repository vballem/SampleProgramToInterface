plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    jacoco
}

android {
    namespace = "com.example.amiiboapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.amiiboapp"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    sourceSets.maybeCreate("test").apply {
        resources.srcDirs("src/test/resources")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }



    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.jvmArgs("-noverify")
            }
        }
    }

    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")

        stabilityConfigurationFile
        rootProject.layout.projectDirectory.file("stability_config.conf")
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.nav)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.junit.ktx)
    ksp(libs.room.compiler)
    implementation(libs.gson)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.core.ktx)
    testImplementation(libs.testng)
    testImplementation(libs.testng)
    ksp(libs.hilt.compiler)

    implementation(libs.coil.compose)
    implementation(libs.squareup.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.io.mockk)

    testImplementation(libs.mockwebserver)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core)
    testImplementation(libs.room.testing)
}

hilt {
    enableAggregatingTask = false
}

jacoco {
    toolVersion = "0.8.10" // Use the latest version
}

tasks.register<JacocoReport>("jacocoTestReport") {
    //dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**_HiltModules*.*",
        "**_HiltModules*.*",
        "**/org/robolectric/shadows/**"
    )

    val debugTree = fileTree("${buildDir}/intermediates/javac/debug") {
        exclude(fileFilter)
    }

    val kotlinDebugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    val mainSrc = "${project.projectDir}/src/main/java"


    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree, kotlinDebugTree))
    executionData.setFrom(fileTree(buildDir) {
        include("**/*.exec", "**/*.ec")
    })
}