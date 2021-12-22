plugins {
    kotlin("jvm") version "1.6.10"
}

group = "com.github.gianttreelp.proguardservicesmapper"
version = "1.0-SNAPSHOT"

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
