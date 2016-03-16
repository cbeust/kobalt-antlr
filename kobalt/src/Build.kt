import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.publish.bintray
import com.beust.kobalt.project

val project = project {
    name = "kobalt-antlr"
    group = "com.beust"
    artifactId = name
    version = "0.1"

    dependencies {
        compile("com.beust:kobalt-plugin-api:0.682",
                "org.antlr:antlr4:4.5.2-1")
    }

    assemble {
        mavenJars {
            fatJar = true
        }
    }

    bintray {
        publish = true
    }

}

