import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    application
    kotlin("jvm")
}

group = parent?.group ?: "com.github.gianttreelp"
version = parent?.version ?: "1.2-SNAPSHOT"

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
