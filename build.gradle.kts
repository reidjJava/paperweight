tasks.register("printVersion") {
    doFirst {
        println(version)
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

allprojects {
    repositories {
        mavenCentral()
        maven {
            name = "PaperMC"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            name = "NexusRepository"
            url = uri("https://bottleup-back.online/repository/maven-snapshots/")
            credentials {
                username = "admin"
                password = "azxaewef345t"
            }
        }
    }
}
