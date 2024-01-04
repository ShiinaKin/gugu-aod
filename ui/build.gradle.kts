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
    implementation(compose.desktop.currentOs)
    api(compose.foundation)
    api(compose.animation)
    api("moe.tlaster:precompose:1.5.10")
    // https://mvnrepository.com/artifact/uk.co.caprica/vlcj
    implementation("uk.co.caprica:vlcj:4.8.2")
}

compose.desktop {
    application {
        mainClass = "ski/mashiro/MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "gugu-aod"
            packageVersion = "1.0.0"
        }
    }
}