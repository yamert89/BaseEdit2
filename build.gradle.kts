plugins {
    kotlin("jvm") version "1.3.61"
    id("com.github.johnrengelman.shadow") version("5.2.0")
    java
}

group = "BaseEdit2"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
    flatDir{
        dirs("lib")
    }
}

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.2.0")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("no.tornado:tornadofx:1.7.19")
    implementation("org.openjfx:javafx-base:13")
    implementation(files (
        "lib/javafx.base.jar",
        "lib/javafx.controls.jar",
        "lib/javafx.fxml.jar",
        "lib/javafx.graphics.jar",
        "lib/javafx.media.jar",
        "lib/javafx.web.jar",
        "lib/javafx.swt.jar"

    ))

}



tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    /*jar{
        manifest {
            attributes("Main-Class" to "main.kotlin.app.BaseEdit2AppKt")
        }

        from {
            configurations.implementation.
            configurations.compile.collect { it.isDirectory() ? it : zipTree(it) }
        }
    }*/
    shadowJar{
        archiveName = "BaseEdit2"
        manifest {
            attributes("Main-Class" to "main.kotlin.app.BaseEdit2AppKt")
        }
    }




}

