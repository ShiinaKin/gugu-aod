import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":backend"))
    implementation(project(":model"))
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    // https://mvnrepository.com/artifact/androidx.navigation/navigation-compose
    api(compose.foundation)
    api(compose.animation)
    api("moe.tlaster:precompose:1.5.10")
}

compose.desktop {
    application {
        mainClass = "ski/mashiro/MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "gugu-mod"
            packageVersion = "1.0.0"
        }
    }
}