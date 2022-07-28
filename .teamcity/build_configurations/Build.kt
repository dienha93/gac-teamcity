package build_configurations

import build_templates.BuildTemplate
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType

class Build(id: String, name: String) : BuildType({
    id(id = "${id}")
    this.name = name
    templates(BuildTemplate.template)
})