pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "PaperMC"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            url = uri("https://bottleup-back.online/repository/maven-snapshots/")
            credentials {
                username = "admin"
                password = "azxaewef345t"
            }
            name = "nexus"
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://bottleup-back.online/repository/maven-snapshots/")
            credentials {
                username = "admin"
                password = "azxaewef345t"
            }
            name = "nexus"
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "paperweight"

include("paperweight-core", "paperweight-lib", "paperweight-userdev")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
