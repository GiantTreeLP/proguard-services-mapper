plugins {
    kotlin("jvm") version "2.0.10"
}

group = "com.github.gianttreelp.proguardservicesmapper"
version = "1.2-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    project.gradle.projectsEvaluated {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "ossrh"
                    url = uri(
                        if (this@subprojects.version.toString().endsWith("SNAPSHOT")) {
                            "https://oss.sonatype.org/content/repositories/snapshots"
                        } else {
                            "https://oss.sonatype.org/service/local/staging/deploy/maven2"
                        }
                    )
                    credentials {
                        username = project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USER")
                        password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
                    }
                }
            }
        }

        val publications: PublicationContainer = extensions.getByType<PublishingExtension>().publications

        publications.withType<MavenPublication>().forEach {
            it.pom {
                name.set(project.name)
                description.set("A utility to map Java services to their obfuscated counterparts.")
                url.set("https://github.com/GiantTreeLP/proguard-services-mapper")
                packaging = "jar"

                scm {
                    connection.set("scm:git:https://github.com/GiantTreeLP/proguard-services-mapper")
                    developerConnection.set("scm:git:https://github.com/GiantTreeLP")
                    url.set("https://github.com/GiantTreeLP/proguard-services-mapper")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("gianttreelp")
                        name.set("Marvin K")
                        email.set("webmaster@gianttree.de")
                    }
                }
            }
        }

        configure<SigningExtension> {
            useGpgCmd()
            val signingKey =
                project.findProperty("signingKey") as String? ?: System.getenv("GPG_PRIVATE_KEY")?.replace("\\n", "\n")
            val signingKeyPassphrase =
                project.findProperty("signingPassphrase") as String? ?: System.getenv("GPG_PASSPHRASE")
            useInMemoryPgpKeys(signingKey, signingKeyPassphrase)
            sign(publications)
        }
    }
}
