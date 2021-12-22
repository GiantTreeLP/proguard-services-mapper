package de.gianttree.proguardservicesmapper.common

import proguard.obfuscate.MappingReader
import java.nio.file.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import kotlin.io.path.exists
import kotlin.io.path.isReadable
import kotlin.io.path.isRegularFile
import kotlin.io.path.outputStream
import kotlin.random.Random
import kotlin.random.nextUInt

const val SERVICES_PATH = "META-INF/services/"

fun mapServices(mappingFileName: String, inputFileName: String) {
    val mappingPath = Paths.get(mappingFileName)
    validatePath(mappingPath, true)
    val inputPath = Paths.get(inputFileName)
    validatePath(inputPath)
    // Temporary output file
    val outputFile = inputPath.resolveSibling("${inputPath.fileName}.${Random.nextUInt()}.jar")

    JarFile(inputPath.toFile()).use { jarFile ->

        // Read META-INF/services/
        if (!scanServices(jarFile, inputPath)) {
            return
        }

        val extractor = readMappingFile(mappingPath)

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
                    val newName = obfuscateFileName(extractor, name)

                    val mappedLines = obfuscateFileContent(jarFile, entry, extractor)

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

private fun obfuscateFileContent(
    jarFile: JarFile,
    entry: JarEntry,
    extractor: MappingExtractor
): ByteArray {
    return jarFile.getInputStream(entry).reader().readLines().joinToString("\n") { line ->
        println("Mapping $line to ${extractor.classMap[line]}")
        extractor.classMap[line] ?: line
    }.encodeToByteArray()
}

private fun obfuscateFileName(
    extractor: MappingExtractor,
    name: String
): String? {
    return extractor.classMap[name.substringAfterLast('/')]?.let {
        name.substringBeforeLast('/') + '/' + it
    }
}

private fun scanServices(jarFile: JarFile, inputPath: Path?): Boolean {
    val services = readServices(jarFile)

    if (services.isEmpty()) {
        println("No services found in $inputPath. Nothing to do.")
        return false
    }
    println("Found ${services.size} services in $inputPath:")
    services.forEach { println("\t$it") }
    return true
}

private fun readMappingFile(mappingPath: Path): MappingExtractor {
    val reader = MappingReader(mappingPath.toFile())
    val extractor = MappingExtractor()
    reader.pump(extractor)
    return extractor
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
