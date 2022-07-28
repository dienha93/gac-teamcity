package build_configurations

import build_templates.DeployTemplate
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType

class Deploy(id: String, name: String) : BuildType({
    id(id = "${id}")
    this.name = name
    templates(DeployTemplate.template)
})
