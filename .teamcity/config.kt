object config {
    val projectBaseName = "VerdiaiScenarioManagement"
    val projectBaseDescription = "Verdiai Scenario Management"
    val parameters = linkedMapOf<String, HashMap<String, String>>(
            "root" to hashMapOf(
                    "env.WorkingDirectory" to ".",
                    "StackName" to "ScenarioManagement"
            ),
            "DEV" to hashMapOf(
                    "env.ArtifactBucketName" to "dev-verdiai-app-launcher",
                    "env.Environment" to "dev",
                    "env.AWSAccountId" to "304022050854",
                    "env.AWSRegion" to "ap-southeast-2",
                    "env.AWSRoleName" to "customer_teamcity_agent_assume_role"
            ),
            "SIT" to hashMapOf(
                    "env.ArtifactBucketName" to "sit-verdiai-app-launcher",
                    "env.Environment" to "sit",
                    "env.AWSAccountId" to "304022050854",
                    "env.AWSRegion" to "ap-southeast-2",
                    "env.AWSRoleName" to "customer_teamcity_agent_assume_role"
            ),
            "UAT" to hashMapOf(
                    "env.ArtifactBucketName" to "uat-verdiai-app-launcher",
                    "env.Environment" to "uat",
                    "env.AWSAccountId" to "535605961859",
                    "env.AWSRegion" to "ap-southeast-2",
                    "env.AWSRoleName" to "customer_teamcity_agent_assume_role"
            ),
            "PRD" to hashMapOf(
                    "env.ArtifactBucketName" to "prd-verdiai-app-launcher",
                    "env.Environment" to "prod",
                    "env.AWSAccountId" to "936719591676",
                    "env.AWSRegion" to "ap-southeast-2",
                    "env.AWSRoleName" to "customer_teamcity_agent_assume_role"
            )
    )
    val projects = LinkedHashMap<String, ArrayList<String>>(
            linkedMapOf(
                    "DEV" to arrayListOf("BUILD", "DEPLOY"),
                    "SIT" to arrayListOf("PREPARATION", "DEPLOY"),
                    "UAT" to arrayListOf("PREPARATION", "DEPLOY"),
                    "PRD" to arrayListOf("PREPARATION", "DEPLOY")
            )
    )
}