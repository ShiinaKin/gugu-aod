plugins {
    kotlin("jvm")
}

repositories {
    mavenLocal()
    maven("https://mirrors.tencent.com/nexus/repository/maven-public")
    mavenCentral()
}

subprojects {
    group = "ski.mashiro"
    version = "1.1.2"

    apply(plugin = "kotlin")

    repositories {
        mavenLocal()
        maven("https://mirrors.tencent.com/nexus/repository/maven-public")
        mavenCentral()
    }

    dependencies {
        // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
        // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.1")
        // https://mvnrepository.com/artifact/commons-codec/commons-codec
        implementation("commons-codec:commons-codec:1.16.0")
        // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        implementation("org.apache.commons:commons-lang3:3.14.0")
        // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        testImplementation("org.jetbrains.kotlin:kotlin-test")
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(17)
    }
}
