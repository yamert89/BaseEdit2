import kotlin.collections.*
plugins {
    kotlin("jvm") version "1.3.61"
    id( "org.openjfx.javafxplugin") version "0.0.5"
    id("application")
    /*id( "org.beryx.jlink") version("2.12.0")*/
    java
}

group = "BaseEdit2"
version = "1.0-SNAPSHOT"

java{
    version = JavaVersion.VERSION_11
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

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
    implementation("org.openjfx:javafx-controls:13")
    implementation("org.openjfx:javafx-graphics:13")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.61")
    /*implementation("org.openjfx:javafx-base:13")
    implementation(files (
        "lib/javafx.base.jar",
        "lib/javafx.controls.jar",
        "lib/javafx.fxml.jar",
        "lib/javafx.graphics.jar",
        "lib/javafx.media.jar",
        "lib/javafx.web.jar",
        "lib/javafx.swt.jar"

    ))*/

}

application.mainClassName = "main.kotlin.app.BaseEdit2AppKt"
application.applicationDefaultJvmArgs.plus("--add-exports java.base/com.sun.glass.ui=ALL-UNNAMED")

javafx{
    modules.add("javafx.controls")
    modules.add("javafx.base")
    modules.add("javafx.graphics")

}


/*mainClassName = "$moduleName/org.openjfx.MainApp"*/


/*jlink {
    options.add("--strip-debug")
    options.add("--compress")
    options.add("2")
    options.add("--no-header-files")
    options.add("--no-man-pages")

    launcher {
        name = "BaseEdit2"
    }
}*/


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    startScripts{
        mainClassName = "app.BaseEdit2Kt"
    }
    build{

    }

    /*run{
        mainClasses.name.plus("app.BaseEdit2Kt")
        classes.name.plus()
    }*/

    jar{
        manifest {
            attributes.put("Main-Class", "app.BaseEdit2Kt")
        }

    }
}