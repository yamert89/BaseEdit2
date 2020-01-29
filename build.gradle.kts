plugins {
    kotlin("jvm") version "1.3.61"
}

group = "BaseEdit2"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir{
        dirs("lib")
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
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}