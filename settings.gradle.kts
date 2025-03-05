pluginManagement {
    repositories {
        mavenLocal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/public")
        maven {
            credentials {
                username = "6661af0c55d469d21fb755c5"
                password = "=3o4EjHlYVT_"
            }
            url = uri("https://packages.aliyun.com/6661af21262ae18c31667f7d/maven/kronos-orm")
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://s01.oss.sonatype.org/content/groups/public")
        maven {
            credentials {
                username = "6661af0c55d469d21fb755c5"
                password = "=3o4EjHlYVT_"
            }
            url = uri("https://packages.aliyun.com/6661af21262ae18c31667f7d/maven/kronos-orm")
        }
    }
}

rootProject.name = "Medal"
include(":app")