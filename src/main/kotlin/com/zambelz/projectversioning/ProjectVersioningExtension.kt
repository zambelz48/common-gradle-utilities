package com.zambelz.projectversioning

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

internal data class VersionOutput(
    val code: Int,
    val name: String
)

@Suppress("UnstableApiUsage")
open class ProjectVersioningExtension(
    objects: ObjectFactory
) {
    val versionFilePath: Property<String> = objects.property(String::class.java)

    fun code(): Int {
        val versionOutput = getSemanticVersion()
        return versionOutput.code
    }

    fun name(): String {
        val versionOutput = getSemanticVersion()
        return versionOutput.name
    }

    @Throws(
        FileNotFoundException::class,
        IOException::class,
        NumberFormatException::class
    )
    private fun getSemanticVersion(): VersionOutput {

        val filePath = this.versionFilePath.getOrElse("version.properties")
        val file = FileInputStream(File(filePath))
        val props = Properties()
        props.load(file)

        val major = props["major"].toString().toInt()
        val minor = props["minor"].toString().toInt()
        val patch = props["patch"].toString().toInt()

        val versionCode = (major * 100) + (minor * 10) + patch
        val versionName = "${major}.${minor}.${patch}"

        return VersionOutput(versionCode, versionName)
    }
}
