package com.beust.kobalt.plugin.antlr

import com.beust.kobalt.api.Project

/**
 * The content of the antlr{} directive, which defines how ANTLR runs.
 */
class AntlrConfig(val project: Project) {
    /**
     * The location of ANTLR grammars.
     */
    var antlrSourceDir = "src/main/antlr"

    /**
     * Where ANTLR generates its source files from the grammars you provide.
     */
    var generatedSourceDir = "src/generated/antlr"

    /**
     * The name of the source set, used in the last segment of `src/main` or `src/test`, e.g. `src/main/antlr`.
     */
    var setName = "antlr"

}

