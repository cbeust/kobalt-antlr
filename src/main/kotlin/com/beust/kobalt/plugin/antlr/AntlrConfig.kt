package com.beust.kobalt.plugin.antlr

import com.beust.kobalt.api.Project

/**
 * The content of the antlr{} directive.
 */
class AntlrConfig(val project: Project) {
    // e.g. src/main/antlr
    var setName = "antlr"
    var generatedSourceDir = "src/generated/antlr"
    var antlrSourceDir = "src/main/antlr"
}

