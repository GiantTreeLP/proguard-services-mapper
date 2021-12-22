plugins {
    application
    kotlin("jvm")
}

group = parent?.group ?: "com.github.gianttreelp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":proguard-services-mapper-common"))
    implementation("org.jetbrains.kotlinx", "kotlinx-cli", "0.3.4")
    implementation(kotlin("stdlib-jdk8"))
}

application {
    mainClass.set("com.github.gianttreelp.proguardservicesmapper.cli.ProGuardServicesMapperKt")
}
