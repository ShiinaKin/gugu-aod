dependencies {
    implementation(project(":common"))
    implementation(project(":model"))
    // https://mvnrepository.com/artifact/org.apache.commons/commons-compress
    implementation("org.apache.commons:commons-compress:1.25.0")
    // https://mvnrepository.com/artifact/org.brotli/dec
    implementation("org.brotli:dec:0.1.2")
    // https://mvnrepository.com/artifact/com.github.ben-manes.caffeine/caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
}