import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.kover)
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**", "**/generated/**")
        ktlint(libs.versions.ktlint.get())
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
    }
}

kover {
    reports {
        filters {
            excludes {
                annotatedBy(
                    "androidx.compose.ui.tooling.preview.Preview",
                    "org.jetbrains.compose.ui.tooling.preview.Preview",
                )
                classes(
                    "*.BuildConfig",
                    "*ComposableSingletons*",
                    "*.R",
                    "*.R\$*",
                )
                packages("adianta.shared.generated.resources")
                classes(
                    "*_androidKt",
                    "*.AndroidPlatform",
                )
            }
        }
        total {
            verify {
                rule("Cobertura minima de linhas") {
                    minBound(50)
                }
            }
        }
    }
}

dependencies {
    kover(project(":shared"))
}

subprojects {
    apply(plugin = "dev.detekt")
    apply(plugin = "org.jetbrains.kotlinx.kover")

    extensions.configure<DetektExtension> {
        parallel = true
        buildUponDefaultConfig = true
        autoCorrect = false
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        baseline = file("detekt-baseline.xml")
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = "11"
        exclude { it.file.absolutePath.contains("${File.separator}build${File.separator}") }
        reports {
            html.required = true
            sarif.required = true
            markdown.required = true
            checkstyle.required = true
        }
    }

    tasks.register("detektAll") {
        group = "verification"
        description = "Executa todas as analises do detekt deste modulo"
        dependsOn(tasks.withType<Detekt>())
    }

    tasks.withType<AbstractTestTask>().configureEach {
        val testKind = providers.gradleProperty("testKind").orNull ?: return@configureEach
        filter.isFailOnNoMatchingTests = false
        when (testKind) {
            "unit" -> filter.excludeTestsMatching("*ComponentTest")
            "component" -> filter.includeTestsMatching("*ComponentTest")
            else -> error("testKind invalido: $testKind (use 'unit' ou 'component')")
        }
    }
}
