package de.gianttree.proguardservicesmapper

import proguard.obfuscate.MappingProcessor

class MappingExtractor : MappingProcessor {

    internal val classMap = mutableMapOf<ClassName, ObfuscatedClassName>()


    override fun processClassMapping(className: String, newClassName: String): Boolean {
        classMap[className] = newClassName
        return true
    }

    override fun processFieldMapping(
        className: String,
        fieldType: String,
        fieldName: String,
        newClassName: String,
        newFieldName: String
    ) {

    }

    override fun processMethodMapping(
        className: String,
        firstLineNumber: Int,
        lastLineNumber: Int,
        methodReturnType: String,
        methodName: String,
        methodArguments: String,
        newClassName: String,
        newFirstLineNumber: Int,
        newLastLineNumber: Int,
        newMethodName: String
    ) {

    }
}
