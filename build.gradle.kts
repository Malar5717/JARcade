plugins {
    application
    id("org.graalvm.buildtools.native") version "1.1.14"
}

group = "com.malar"
version = "1.0.0"

repositories {
    mavenCentral()
}

// The project keeps its original layout: sources and resources both live under `src/`,
// with the word lists sitting next to WordLoader in com/malar/jarcade/resource/.
// `WordLoader` loads them as classpath resources relative to its own package.
sourceSets {
    named("main") {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(listOf("src"))
        resources.exclude("**/*.java")
    }
}

// Match the documented/`javac` build: compile with the running JDK but target Java 17.
tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
    options.encoding = "UTF-8"
}

application {
    mainClass = "com.malar.jarcade.JARcade"
}

// Make `gradlew run` interactive (the games read from System.in).
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.malar.jarcade.JARcade"
    }
}

graalvmNative {
    binaries {
        named("main") {
            mainClass.set("com.malar.jarcade.JARcade")
            buildArgs.addAll(
                // Standalone binary: no JVM fallback.
                "--no-fallback",
                // InputProvider holds a static Scanner bound to System.in; it must be
                // initialized at run time, not build time.
                "--initialize-at-run-time=com.malar.jarcade.ui.InputProvider",
            )
        }
    }
}
