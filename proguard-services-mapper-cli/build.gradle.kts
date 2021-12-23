plugins {
    application
    kotlin("jvm")
}

group = parent?.group ?: "com.github.gianttreelp"
version = parent?.version ?: "1.1-SNAPSHOT"

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
