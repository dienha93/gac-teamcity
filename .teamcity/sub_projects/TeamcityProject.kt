package sub_projects

import jetbrains.buildServer.configs.kotlin.v2019_2.Project

class TeamcityProject(id: String, name: String) : Project({
    id(id = id)
    this.name = name
})