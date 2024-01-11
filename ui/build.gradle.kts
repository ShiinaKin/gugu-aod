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
    implementation(project(":common"))
    implementation(project(":model"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    api(compose.foundation)
    api(compose.animation)
    api("moe.tlaster:precompose:1.5.10")
    // https://mvnrepository.com/artifact/uk.co.caprica/vlcj
    implementation("uk.co.caprica:vlcj:4.8.2")
    // https://mvnrepository.com/artifact/io.github.oshai/kotlin-logging-jvm
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.1")
}

compose.desktop {
    application {
        mainClass = "ski.mashiro.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "gugu-aod"
            packageVersion = version as String
            licenseFile.set(project.file("../LICENSE"))

            windows {
                iconFile.set(project.file("src/main/resources/icon.ico"))
                dirChooser = true
            }
        }
    }
}