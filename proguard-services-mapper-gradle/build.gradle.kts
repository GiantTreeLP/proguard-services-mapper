plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = parent?.group ?: "com.github.gianttreelp"
version = parent?.version ?: "1.2-SNAPSHOT"

dependencies {
    implementation(gradleApi())
    implementation(project(":proguard-services-mapper-common"))
}

java {
    withJavadocJar()
    withSourcesJar()
}
