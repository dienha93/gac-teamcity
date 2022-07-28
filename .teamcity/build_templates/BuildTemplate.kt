package build_templates

import config
import jetbrains.buildServer.configs.kotlin.v2019_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2019_2.Template
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

object BuildTemplate {
    var template = Template {
        id("${config.projectBaseName}BuildTemplate")
        name = "${config.projectBaseDescription} Build Template"
        description = "${config.projectBaseDescription} Build Template"

        params {
            text(name = "MyParam", value = "", allowEmpty = true)
        }
        vcs {
            root(DslContext.settingsRoot)
            branchFilter = """
                +:*
            """.trimIndent()
            cleanCheckout = true
        }

        steps {
            script {
                name = "Install tools and dependencies"
                id = "${config.projectBaseName}BuildInstallTools"
                scriptContent = """
                #!/bin/bash -x
                echo "input here"
            """.trimIndent()
            }

            script {
                name = "Test"
                id = "${config.projectBaseName}BuildTest"
                scriptContent = """
                #!/bin/bash -x                    
                # npm run test
                echo "input here"
            """.trimIndent()
            }

            script {
                name = "Package"
                id = "${config.projectBaseName}BuildPackage"
                scriptContent = """
                #!/bin/bash -x
                echo "##teamcity[setParameter name='ArtifactName' value='${'$'}{package_name}.zip']"
                
            """.trimIndent()
            }
        }
    }

}