pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // The Compose repository you had is still included
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        // JitPack is required for SceneView
        maven("https://jitpack.io")
    }
}

rootProject.name = "MamboVirtualTour"
include(":app")
