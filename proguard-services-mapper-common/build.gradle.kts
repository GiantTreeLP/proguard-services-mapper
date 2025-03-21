import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = parent?.group ?: "com.github.gianttreelp"
version = parent?.version ?: "1.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("com.guardsquare", "proguard-base", "7.6.1")
    implementation(kotlin("stdlib-jdk8"))
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}
