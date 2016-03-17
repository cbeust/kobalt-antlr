#  ANTLR plug-in for Kobalt

This is a plug-in for the [Kobalt build system](http://beust.com/kobalt) that lets you parse and generate code for ANTLR grammars.

# Installing

## Existing project

Add the following to your dependencies in your `Build.kt` file:

```
    dependencies {
        compile("com.beust:kobalt-antlr:0.2")
    }
```

## Brand new project
 
This plug-in exposes a template that will create a full project with an example grammar in it. To run it:

1. [Install Kobalt](http://beust.com/kobalt/getting-started/index.html#installing)
2. Invoke the template as follows:

```
$ kobaltw --plugins com.beust:kobalt-antlr: --init antlr
Template "antlr" installed
Build this project with `./kobaltw assemble`
```

This command tells Kobalt to download the `kobalt-antlr` plug-in and then to run the template named "antlr". This will generate a simple project with
a few sample files (look into the `src` directory). We can now build this project:

```
$ kobaltw assemble
          ╔═══════════════════════════════╗
          ║ Building kobalt-antlr-example ║
          ╚═══════════════════════════════╝
───── kobalt-antlr-example:generateGrammarSource
───── kobalt-antlr-example:compile
  Java compiling 4 files
───── kobalt-antlr-example:assemble
  Created ./kobaltBuild/libs/kobalt-antlr-example-0.1.jar
  Created ./kobaltBuild/libs/kobalt-antlr-example-0.1-sources.jar
  Wrote ./kobaltBuild/libs/kobalt-antlr-example-0.1.pom
BUILD SUCCESSFUL (4 seconds)
```

## Using the plug-in

In order to trigger ANTLR parsing in your build file, you need to import and invoke the `antlr{}` directive:

```
import com.beust.kobalt.plugin.antlr.*

val p = project {
    // ...

    antlr {
    }
}
```

This directive is represented by the [AntlrConfig](https://github.com/cbeust/kobalt-antlr/blob/master/src/main/kotlin/com/beust/kobalt/plugin/antlr/AntlrConfig.kt) class and contains additional options that you can override, such as the location where grammars can be found and where the sources get generated. Please consult the [AntlrConfig](https://github.com/cbeust/kobalt-antlr/blob/master/src/main/kotlin/com/beust/kobalt/plugin/antlr/AntlrConfig.kt) class for the full list of options/





