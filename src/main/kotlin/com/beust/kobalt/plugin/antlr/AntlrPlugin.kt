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

@Singleton
class AntlrPlugin @Inject constructor(override val configActor: ConfigActor<AntlrConfig>)
: BaseJvmPlugin<AntlrConfig>(configActor), ISourceDirectoryContributor, IClasspathContributor, ITemplateContributor {

    class Template : JarTemplate("templates/antlr.jar") {
        override val pluginName: String
            get() = AntlrPlugin.PLUGIN_NAME

        override val templateDescription: String
            get() = "Simple ANTLR project"

        override val templateName: String
            get() = "antlr"

    }

    override val templates = listOf(Template())

    // IClasspathContributor
    override fun classpathEntriesFor(project: Project?, context: KobaltContext)
        = listOf(context.dependencyManager.createMaven("org.antlr:antlr4:4.5.2-1"))

    override fun flagsFor(project: Project, context: KobaltContext, currentFlags: List<String>,
            suffixesBeingCompiled: List<String>) = emptyList<String>()

    companion object {
        const val PLUGIN_NAME = "Antlr"
    }

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

    // ISourceDirectoryContributor
    val sourceDirectories = arrayListOf<File>()
    override fun sourceDirectoriesFor(project: Project, context: KobaltContext) = sourceDirectories
}

class AntlrConfig(val project: Project) {
    // e.g. src/main/antlr
    var setName = "antlr"
    var generatedSourceDir = "src/generated/antlr"
    var antlrSourceDir = "src/main/antlr"
}

@Directive
fun Project.antlr(init: AntlrConfig.() -> Unit) = let { project ->
    AntlrConfig(project).apply {
        init()
        (Kobalt.findPlugin(AntlrPlugin.PLUGIN_NAME) as AntlrPlugin).addConfiguration(project, this)
    }
}
