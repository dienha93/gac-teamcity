package build_templates

import config
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildTypeSettings
import jetbrains.buildServer.configs.kotlin.v2019_2.DslContext
import jetbrains.buildServer.configs.kotlin.v2019_2.Template
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

object DeployTemplate {
    val template = Template {
        id("${config.projectBaseName}DeployTemplate")
        name = "${config.projectBaseDescription} Deploy Template"
        description = "${config.projectBaseDescription} Deploy Template"
        vcs {
            root(DslContext.settingsRoot)
            branchFilter = """
                +:*
            """.trimIndent()
            cleanCheckout = true
        }
        params {
            text(name = "MyParam", value = "", allowEmpty = false)
        }
        type = BuildTypeSettings.Type.DEPLOYMENT
        steps {
            script {
                id = "${config.projectBaseName}DeployServerlessPlugin"
                name = "Install Serverless Plugin"
                scriptContent = """
                    #!/bin/bash -x
                    echo "input here"
                """.trimIndent()
            }
            script {
                id = "${config.projectBaseName}DeployProfileConfiguration"
                name = "Config AWS Profile"
                scriptContent = """
                    #!/bin/bash -x
                    echo "input here"
                """.trimIndent()
            }
            }
        }
    }
}