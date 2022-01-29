package com.github.gianttreelp.proguardservicesmapper.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

@Suppress("unused")
class ProguardServicesMapperPlugin : Plugin<Project> {

    private inline fun <reified T : Task> TaskContainer.register(
        name: String,
        noinline configuration: T.() -> Unit
    ) = this.register(name, T::class.java, configuration)

    override fun apply(target: Project) {
        target.tasks.register<ProguardServicesMapperTask>("proguardServicesMapper") {
            this.group = "Proguard"
            this.description = "Generates a mapping file for Proguard to use for obfuscating services."
        }
    }
}
