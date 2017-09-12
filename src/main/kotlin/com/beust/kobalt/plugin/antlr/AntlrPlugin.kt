package com.beust.kobalt.plugin.antlr

import com.beust.kobalt.TaskResult
import com.beust.kobalt.api.*
import com.beust.kobalt.api.annotation.Directive
import com.beust.kobalt.api.annotation.Task
import com.beust.kobalt.internal.BaseJvmPlugin
import com.beust.kobalt.misc.KFiles
import com.beust.kobalt.misc.log
import com.google.inject.Inject
import com.google.inject.Singleton
import org.antlr.v4.Tool
import java.io.File

/**
 * A simple ANTLR plug-in for Kobalt. Triggered by antlr{} i your build file. Refer to AntlrConfig below
 * for a list of the options you can specify inside that directive.
 *
 * @author Cedric Beust (cedric@beust.com)
 * @since 3/17/2016
 */
@Singleton
class AntlrPlugin @Inject constructor(override val configActor: ConfigActor<AntlrConfig>)
    : BaseJvmPlugin<AntlrConfig>(configActor), ISourceDirectoryContributor, IClasspathContributor,
        ITemplateContributor {

    companion object {
        const val PLUGIN_NAME = "Antlr"
    }

    override fun sourceSuffixes(): List<String> = listOf("g4", "tokens")

    // ICompilerFlagContributor
    override fun compilerFlagsFor(
            project: Project,
            context: KobaltContext,
            currentFlags: List<String>,
            suffixesBeingCompiled:
            List<String>
    ) = emptyList<String>()

    override val name = PLUGIN_NAME

    @Task(name = "generateGrammarSource", runBefore = arrayOf("compile"))
    fun taskGenerateGrammarSource(project: Project) : TaskResult {
        val config = configurationFor(project)
        if (config != null) {
            val grammarFiles = KFiles.findRecursively(File(config.antlrSourceDir), { it.endsWith(".g4") })
            log(2, "  Found ${grammarFiles.size} grammar files")
            val outputDir = KFiles.joinDir(project.directory, config.generatedSourceDir)
            val args = arrayOf("-o", outputDir, *grammarFiles.toTypedArray())
            val tool = Tool(args)
            tool.processGrammarsOnCommandLine()
            val hasErrors = tool.errMgr.numErrors > 0
            if (! hasErrors) {
                sourceDirectories.add(File(project.directory, config.generatedSourceDir))
            }
            return TaskResult(! hasErrors)
        } else {
            log(2, "Didn't find any antlr{} directive, not running antlr")
            return TaskResult()
        }
    }

    // IClasspathContributor

    val simpleTemplate = object: FileJarTemplate("templates/antlr.jar") {
        override val pluginName = AntlrPlugin.PLUGIN_NAME
        override val templateDescription = "Simple ANTLR project"
        override val templateName = "antlr"
    }

    // Somehow, inlining this variable here causes
    // Error:(61, 18) Kotlin: Property effective visibility 'public' should be the same or less permissive than
    // its type effective visibility 'local'
    override val templates = listOf(simpleTemplate)

    override fun classpathEntriesFor(project: Project?, context: KobaltContext)
            = listOf(context.dependencyManager.createMaven("org.antlr:antlr4:4.5.2-1"))

    // ISourceDirectoryContributor
    val sourceDirectories = arrayListOf<File>()
    override fun sourceDirectoriesFor(project: Project, context: KobaltContext) = sourceDirectories
}

@Directive
fun Project.antlr(init: AntlrConfig.() -> Unit) = let { project ->
    AntlrConfig(project).apply {
        init()
        (Kobalt.findPlugin(AntlrPlugin.PLUGIN_NAME) as AntlrPlugin).addConfiguration(project, this)
    }
}
