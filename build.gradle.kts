import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm") version "1.6.10"
}

group = "de.gianttree"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // https://mvnrepository.com/artifact/com.guardsquare/proguard-retrace
    implementation("com.guardsquare", "proguard-base", "7.2.0-beta4")
    implementation("org.jetbrains.kotlinx", "kotlinx-cli", "0.3.4")
    implementation(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

application {
    mainClass.set("de.gianttree.proguardservicesmapper.ProGuardServicesMapperKt")
}
