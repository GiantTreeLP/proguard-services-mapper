plugins {
    kotlin("jvm")
}

group = "de.gianttree"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.guardsquare/proguard-retrace
    api("com.guardsquare", "proguard-base", "7.2.0-beta4")
    implementation(kotlin("stdlib-jdk8"))
}
