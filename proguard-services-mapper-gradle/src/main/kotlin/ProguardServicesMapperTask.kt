package com.github.gianttreelp.proguardservicesmapper.gradle

import com.github.gianttreelp.proguardservicesmapper.common.mapServices
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class ProguardServicesMapperTask : DefaultTask() {

    @get:InputFile
    abstract val mappingFile: RegularFileProperty

    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @TaskAction
    fun map() {
        val mapping = this.mappingFile.get().asFile
        val input = this.inputFile.get().asFile

        mapServices(mapping.absolutePath, input.absolutePath)
    }
}
