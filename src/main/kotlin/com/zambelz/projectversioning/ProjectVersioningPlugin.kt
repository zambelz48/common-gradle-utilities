package com.zambelz.projectversioning

import org.gradle.api.Plugin
import org.gradle.api.Project

open class ProjectVersioningPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create(
            "projectVersionConfig",
            ProjectVersioningExtension::class.java
        )
    }

}
