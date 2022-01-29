plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = parent?.group ?: "com.github.gianttreelp"
version = parent?.version ?: "1.1"

dependencies {
    implementation(gradleApi())
    implementation(project(":proguard-services-mapper-common"))
}

gradlePlugin {
    plugins {
        create("ProguardServicesMapper") {
            id = "com.github.gianttreelp.proguardservicesmapper.gradle"
            implementationClass = "com.github.gianttreelp.proguardservicesmapper.gradle.ProguardServicesMapperPlugin"
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
