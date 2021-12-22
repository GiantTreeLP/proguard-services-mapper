package de.gianttree.proguardservicesmapper.common

import proguard.obfuscate.MappingReader
import java.nio.file.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import kotlin.io.path.*
import kotlin.random.Random
import kotlin.random.nextUInt

const val SERVICES_PATH = "META-INF/services/"

fun mapServices(mappingFileName: String, inputFileName: String) {
    val mappingPath = Paths.get(mappingFileName)
    validateInputPath(mappingPath)
    val inputPath = Paths.get(inputFileName)
    validateInputPath(inputPath)
    // Temporary output file
    val outputFile = inputPath.resolveSibling("${inputPath.fileName}.${Random.nextUInt()}.jar")
    validateOutputPath(outputFile)

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

            outputStream.setLevel(9)

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
        val newName = extractor.classMap[line] ?: line
        if (newName != line) {
            println("Mapping $line to $newName")
        } else {
            println("NOT MAPPING $line")
        }
        newName
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

fun validateInputPath(path: Path) {
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

fun validateOutputPath(path: Path) {
    if (path.exists()) {
        throw IllegalArgumentException("Path $path already exists")
    }
    if (!path.parent.exists()) {
        throw IllegalArgumentException("Path ${path.parent} does not exist")
    }
    if (!path.parent.isDirectory()) {
        throw IllegalArgumentException("Path ${path.parent} is not a directory")
    }
}
