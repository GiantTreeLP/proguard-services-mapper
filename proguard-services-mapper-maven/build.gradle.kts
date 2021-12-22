plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "de.gianttree"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":proguard-services-mapper-common"))
    implementation(kotlin("stdlib"))
    compileOnly("org.apache.maven", "maven-plugin-api", "3.8.4")
    compileOnly("org.apache.maven.plugin-tools", "maven-plugin-annotations", "3.6.2")
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
