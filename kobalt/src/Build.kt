import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.publish.bintray
import com.beust.kobalt.project

val project = project {
    name = "kobalt-antlr"
    group = "com.beust"
    artifactId = name
    version = "0.3"

    dependencies {
        compile("com.beust:kobalt-plugin-api:1.0.68",
                "org.antlr:antlr4:4.7")
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

