plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = parent?.group ?: "com.github.gianttreelp"
version = parent?.version ?: "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("com.guardsquare", "proguard-base", "7.2.0-beta4")
    implementation(kotlin("stdlib-jdk8"))
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
