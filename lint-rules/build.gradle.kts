plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "org.alias.lint.StringFormatIssueRegistry")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    val lintVersion = "31.2.0" // gradle plugin version + 23
    implementation("com.android.tools.lint:lint-api:$lintVersion")
    compileOnly("com.android.tools.lint:lint-checks:$lintVersion")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.android.tools.lint:lint-tests:$lintVersion")
}