/*
 * paperweight is a Gradle plugin for the PaperMC project. It uses
 * some code and systems originally from ForgeGradle.
 *
 * Copyright (C) 2020 Kyle Wood
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package io.papermc.paperweight.tasks.patchremap

import io.papermc.paperweight.tasks.sourceremap.ConstructorsData
import io.papermc.paperweight.tasks.sourceremap.ParamNames
import io.papermc.paperweight.tasks.sourceremap.PatchParameterRemapper
import io.papermc.paperweight.tasks.sourceremap.SrgParameterRemapper
import org.cadixdev.lorenz.MappingSet
import org.cadixdev.mercury.Mercury
import org.cadixdev.mercury.remapper.MercuryRemapper
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption.REPLACE_EXISTING

class PatchSourceRemapWorker(
    private val mappings: MappingSet,
    private val classpath: Collection<Path>,
    private val paramNames: ParamNames,
    private val constructorsData: ConstructorsData,
    private val inputDir: Path,
    private val outputDir: Path
) {

    private val reverseMappings: MappingSet = mappings.reverse()

    fun remap() {
        setup()

        Mercury().apply {
            classPath.addAll(classpath)

            processors.addAll(listOf(
                MercuryRemapper.create(reverseMappings),
                PatchParameterRemapper(paramNames, constructorsData)
            ))

            rewrite(inputDir, outputDir)
        }

        cleanup()
    }

    fun remapBack() {
        setup()

        Mercury().apply {
            classPath.addAll(classpath)

            processors.addAll(listOf(
                MercuryRemapper.create(mappings),
                SrgParameterRemapper(mappings, constructorsData, paramNames)
            ))

            rewrite(inputDir, outputDir)
        }

        cleanup()
    }

    private fun setup() {
        Files.walk(outputDir).use { it.forEach(Files::delete) }
        Files.createDirectories(outputDir)
    }

    private fun cleanup() {
        Files.walk(inputDir).use { it.forEach(Files::delete) }
        Files.walk(outputDir).use {
            it.forEach { src ->
                val dest = inputDir.resolve(outputDir.relativize(src))
                Files.createDirectories(dest.parent)
                Files.copy(src, dest, REPLACE_EXISTING)
            }
        }
    }
}
