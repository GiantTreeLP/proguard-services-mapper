package de.gianttree.proguardservicesmapper

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import proguard.obfuscate.MappingReader
import java.nio.file.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import kotlin.io.path.exists
import kotlin.io.path.isReadable
import kotlin.io.path.isRegularFile
import kotlin.io.path.outputStream

const val SERVICES_PATH = "META-INF/services/"

fun main(args: Array<String>) {
    val parser = ArgParser("proguard-services-mapper")
    val mappingFileName by parser.option(ArgType.String, "mapping", "m", "Mapping file name").required()
    val inputFileName by parser.option(ArgType.String, "input", "i", "Input file name").required()

    parser.parse(args)
    mapServices(mappingFileName, inputFileName)
}

fun mapServices(mappingFileName: String, inputFileName: String) {
    val mappingPath = Paths.get(mappingFileName)
    validatePath(mappingPath, true)
    val inputPath = Paths.get(inputFileName)
    validatePath(inputPath)
    val outputFile = inputPath.resolveSibling("${inputPath.fileName}.mapped.jar")
    JarFile(inputPath.toFile()).use { jarFile ->

        // Read META-INF/services/
        val services = readServices(jarFile)

        if (services.isEmpty()) {
            println("No services found in $inputPath. Nothing to do.")
            return
        }
        println("Found ${services.size} services in $inputPath:")
        services.forEach { println("\t$it") }

        // Read mapping file
        val reader = MappingReader(mappingPath.toFile())
        val extractor = MappingExtractor()
        reader.pump(extractor)

        JarOutputStream(
            outputFile.outputStream(
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            ).buffered()
        ).use { outputStream ->

            for (entry in jarFile.entries()) {
                val name = entry.name
                if (name.startsWith(SERVICES_PATH)) {
                    val newName =
                        extractor.classMap[name.substringAfterLast('/')]?.let {
                            name.substringBeforeLast('/') + '/' + it
                        }

                    val mappedLines = jarFile.getInputStream(entry).reader().readLines().joinToString("\n") { line ->
                        extractor.classMap[line] ?: line
                    }.encodeToByteArray()

                    if (newName != null) {
                        println("Mapping $name to $newName")
                        outputStream.putNextEntry(JarEntry(newName))
                        outputStream.write(mappedLines)
                    } else {
                        println("NOT MAPPING $name")
                        outputStream.putNextEntry(JarEntry(name))
                        outputStream.write(mappedLines)
                    }
                } else {
                    outputStream.putNextEntry(entry)
                    outputStream.write(jarFile.getInputStream(entry).use { it.readBytes() })
                }
            }
        }
    }
    Files.move(outputFile, inputPath, StandardCopyOption.REPLACE_EXISTING)
}

fun readServices(jarFile: JarFile): List<String> {
    val entries = mutableListOf<String>()

    for (entry in jarFile.entries()) {
        if (entry.name.startsWith(SERVICES_PATH)) {
            entries += entry.name
        }
    }

    return entries
}

fun validatePath(path: Path, readOnly: Boolean = false) {
    if (!path.exists()) {
        throw IllegalArgumentException("Path $path does not exist")
    }
    if (!path.isRegularFile()) {
        throw IllegalArgumentException("Path $path is not a regular file")
    }
    if (!path.isReadable()) {
        throw IllegalArgumentException("Path $path is not readable")
    }
}
