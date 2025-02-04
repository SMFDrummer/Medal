plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.kronosGradlePlugin)
}

group = "smf.talkweb"

dependencies {
    testImplementation(kotlin("test"))

    api(libs.bundles.kotlinxEcosystem)
    api(libs.bundles.ktorClient)
    api(libs.bundles.kronos)
    api(libs.bcprov)
    api(libs.reflections)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks {
    processResources {
        from("src/main/resources")
    }
}