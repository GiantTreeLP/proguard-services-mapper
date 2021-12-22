package de.gianttree.proguardservicesmapper.cli

import de.gianttree.proguardservicesmapper.common.mapServices
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

fun main(args: Array<String>) {
    val parser = ArgParser("proguard-services-mapper")
    val mappingFileName by parser.option(ArgType.String, "mapping", "m", "Mapping file name").required()
    val inputFileName by parser.option(ArgType.String, "input", "i", "Input file name").required()

    parser.parse(args)
    mapServices(mappingFileName, inputFileName)
}

