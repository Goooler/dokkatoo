package dev.adamko.dokkatoo.tasks

import dev.adamko.dokkatoo.DokkatooBasePlugin.Companion.jsonMapper
import dev.adamko.dokkatoo.dokka.parameters.DokkaParametersKxs
import javax.inject.Inject
import kotlinx.serialization.encodeToString
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.PathSensitivity.RELATIVE

/**
 * Produces a Dokka Configuration that describes a single module of a multimodule Dokka configuration.
 *
 * @see dev.adamko.dokkatoo.dokka.parameters.DokkaParametersKxs.DokkaModuleDescriptionKxs
 */
@CacheableTask
abstract class DokkatooPrepareModuleDescriptorTask @Inject constructor(
  private val layout: ProjectLayout,
) : DokkatooTask() {

  @get:Input
  abstract val moduleName: Property<String>

  @get:Input
  protected abstract val sourceOutputDirectory: Property<String>

  fun sourceOutputDirectory(path: Any) {
    sourceOutputDirectory.set(
      layout.files(path).singleFile.invariantSeparatorsPath
    )
  }

  @get:InputFiles
  @get:Optional
  @get:PathSensitive(RELATIVE)
  abstract val includes: ConfigurableFileCollection

  @get:OutputFile
  abstract val dokkaModuleDescriptorJson: RegularFileProperty

  @get:Input
  abstract val modulePath: Property<String>

  @TaskAction
  fun generateModuleConfiguration() {
    val moduleName = moduleName.get()
    val sourceOutputDirectory = layout.files(sourceOutputDirectory).singleFile
    val includes = includes.files
    val modulePath = modulePath.get()

    val moduleDesc = DokkaParametersKxs.DokkaModuleDescriptionKxs(
      name = moduleName,
      sourceOutputDirectory = sourceOutputDirectory,
      includes = includes,
      modulePath = modulePath,
    )

    val encodedModuleDesc = jsonMapper.encodeToString(moduleDesc)

    logger.info("encodedModuleDesc: $encodedModuleDesc")

    dokkaModuleDescriptorJson.get().asFile.writeText(encodedModuleDesc)
  }
}
