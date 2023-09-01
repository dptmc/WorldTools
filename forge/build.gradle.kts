architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    forge {
        mixinConfig("worldtools.mixins.common.json")
    }
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        name = "KotlinForForge"
    }
    maven("https://cursemaven.com") {
        name = "Curse"
    }
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentForge"].extendsFrom(this)
}

dependencies {
    forge("net.minecraftforge:forge:${project.properties["forge_version"]!!}")
    implementation("thedarkcolour:kotlinforforge:${project.properties["kotlin_forge_version"]!!}")
    common(project(":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
    implementation(shadowCommon("net.kyori:adventure-text-minimessage:${project.properties["kyori_text_minimessage_version"]}")!!)
    implementation(shadowCommon("net.kyori:adventure-text-serializer-gson:${project.properties["kyori_text_minimessage_version"]}")!!)
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }
}
