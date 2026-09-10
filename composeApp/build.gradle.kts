import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)

    id("com.github.gmazzo.buildconfig") version "6.0.9"
    id("com.github.ben-manes.versions") version "0.54.0"
    id("io.github.kdroidfilter.compose.linux.packagedeps") version "0.2.5"
}

linuxDebConfig {
    debDepends.set(listOf("imagemagick"))

}

buildConfig {
    packageName("widgets.config")

    val localProps = Properties().apply {
        val propsFile = rootProject.file("local.properties")
        if (propsFile.exists()) {
            propsFile.inputStream().use { load(it) }
        }
    }

    buildConfigField("String", "WEATHER_API_KEY", "\"${localProps.getProperty("weather.api.key", "")}\"")
    buildConfigField("String", "HERE_GEOCODING_API_KEY", "\"${localProps.getProperty("here.geocoding.api.key", "")}\"")
}

kotlin {
    jvm()
    jvmToolchain(21)

    tasks.withType<JavaExec>().configureEach {
        standardInput = System.`in`
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(libs.compose.components.resources)
            implementation(libs.geotimezone)
            implementation(libs.suncalc)

            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.dbus.java)
            implementation(libs.dbus.java.transport)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.oshi.core)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.okhttp)

            implementation(libs.compose.material.icons.extended)
            implementation(libs.liquid)

            // JNA
            implementation(libs.jna)
            implementation(libs.jna.platform)

            implementation(libs.im4java)



            implementation("io.coil-kt.coil3:coil-svg:3.5.0-beta01")


            implementation("com.ibm.icu:icu4j:75.1")


        }
    }
}


compose.desktop {
    application {
        mainClass = "widgets.MainKt"


        jvmArgs += listOf(
            "-Dskiko.gpu.resourceCacheLimit=0",
            "-XX:+UseZGC",
            "-XX:+ZGenerational",

            )


        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "widgets"
            packageVersion = "1.0.0"


            modules(
                "java.security.jgss",
                "jdk.security.auth",
                "jdk.localedata"
            )

            linux {
                iconFile.set(project.file("src/jvmMain/composeResources/drawable/icon.png"))
            }


        }

        buildTypes.release.proguard {
            configurationFiles.from("proguard-rules.pro")
        }
    }
}

