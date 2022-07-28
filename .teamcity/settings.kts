import build_configurations.Build
import build_configurations.Deploy
import build_templates.BuildTemplate
import build_templates.DeployTemplate
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import sub_projects.TeamcityProject

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.2"

project {
    description = "The Verdiai Scenario Management project"
    // Add default template
    template(BuildTemplate.template)
    template(DeployTemplate.template)
    params {
        val parametersRoot = config.parameters.getOrDefault("root", hashMapOf())
        for ((paramName, paramValue) in parametersRoot) {
            text(name = paramName, value = paramValue, readOnly = true)
        }
    }
    var listProjectIds = arrayListOf<Id>()
    for ((subProjectName, builds) in config.projects) {
        val subPJ = TeamcityProject(id = "${subProjectName}", name = subProjectName)
        subProject(subPJ)
        listProjectIds.add(RelativeId(relativeId = "${subProjectName}"))
        // Add parameter for sub project level 1
        subPJ.params {
            val parameters = config.parameters.getOrDefault(subProjectName, hashMapOf())
            for ((paramName, paramValue) in parameters) {
                text(name = paramName, value = paramValue, readOnly = true)
            }
        }

        var listBuildIds = arrayListOf<Id>()
        var listBuildTypes = arrayListOf<BuildType>()
        // add build configuration for project level 1
        for (buildName in builds) {
            when (buildName) {
                "BUILD" -> {
                    var build = Build(id = "${subProjectName}_${buildName}", name = buildName)
                    subPJ.buildType(build)
                    listBuildTypes.add(build)
                    listBuildIds.add(RelativeId(relativeId = "${subProjectName}_${buildName}"))
                }
                "DEPLOY" -> {
                    var build = Deploy(id = "${subProjectName}_${buildName}", name = buildName)
                    subPJ.buildType(build)
                    listBuildTypes.add(build)
                    listBuildIds.add(RelativeId(relativeId = "${subProjectName}_${buildName}"))
                }
                "PREPARATION" -> {
                    var build = Build(id = "${subProjectName}_${buildName}", name = buildName)
                    subPJ.buildType(build)
                    listBuildTypes.add(build)
                    listBuildIds.add(RelativeId(relativeId = "${subProjectName}_${buildName}"))
                }
                else -> { // Note the block
                    print("${buildName} is not support")
                }
            }
        }
        // make build chain
        for (index in listBuildTypes.indices) {
            if (index > 0) {
                val first = listBuildTypes.get(index - 1)
                first.artifactRules = "+:%env.WorkingDirectory%/*.zip"
                first.publishArtifacts = PublishMode.NORMALLY_FINISHED

                val current = listBuildTypes.get(index)
                current.dependencies {
                    dependency(first) {
                        snapshot {
                            onDependencyFailure = FailureAction.IGNORE
                            onDependencyCancel = FailureAction.IGNORE
                            synchronizeRevisions = false
                        }
                        artifacts {
                            artifactRules = "+:*.zip"
                        }
                    }
                }
                // passing parameter
                if (current.name.equals("DEPLOY")) {
                    first.triggers {
                        vcs {
                            id = "vcs_trigger_${first.id!!}"
                            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
                            triggerRules = "+:root=${DslContext.settingsRoot.id}:**"
                            branchFilter = """
                                +:reds/heads/master
                                +:reds/heads/release/*
                            """.trimIndent()
                        }
                    }
                    current.triggers {
                        finishBuildTrigger {
                            buildType = "${first.id}"
                            successfulOnly = true
                            branchFilter = """
                                +:*
                            """.trimIndent()
                        }
                    }
                    current.params {
                        text(name = "ArtifactName", value = "%dep.${first.id!!}.ArtifactName%")
                    }
                }
            }
        }
        subPJ.buildTypesOrderIds = listBuildIds
    }
    subProjectsOrder = listProjectIds
}
