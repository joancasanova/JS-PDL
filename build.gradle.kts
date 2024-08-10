import org.gradle.jvm.tasks.Jar

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0-RC3"
}

repositories {
    mavenCentral()
}

allprojects {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<ProcessResources> {
        filteringCharset = "UTF-8"
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
}

tasks.test {
    useJUnitPlatform()
    enabled = false
}

tasks.jar {
    manifest {
        attributes(
            "Manifest-Version" to "1.0",
            "Main-Class" to "main.Analizador"
        )
    }
    archiveBaseName.set("Analizador")
    archiveVersion.set("1.0")
    destinationDirectory.set(layout.buildDirectory.dir("libs").get().asFile)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Tarea personalizada para ejecutar la aplicación
tasks.register<JavaExec>("run") {
    group = "application"
    description = "Run the main class"
    mainClass.set("main.Analizador")
    classpath = sourceSets["main"].runtimeClasspath
    args = listOf("input/input.txt")
}
