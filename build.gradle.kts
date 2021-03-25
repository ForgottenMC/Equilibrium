/*
 * MIT License
 *
 * Copyright 2021 Dusmartijngames and Forbidden-A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */


plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
    /*
    kotlin("jvm") version "1.4.31"
    id("org.jmailen.kotlinter") version "3.3.0"
    */
}

version = "0.1-ALPHA"
group = "net.forgottenmc.equilibrium"
val mainClass = "$group.Equilibrium"

val log4jVersion: String by rootProject
val snakeyamlVersion: String by rootProject
val dotenvVersion: String by rootProject


repositories {
    mavenCentral()
}

dependencies {

    // Config and ENV
    implementation("io.github.cdimascio:dotenv-java:$dotenvVersion")
    implementation("org.yaml:snakeyaml:$snakeyamlVersion")

    // Logging
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
}


tasks {

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    shadowJar {
        mergeServiceFiles()
        manifest {
            attributes(
                "Main-Class" to mainClass
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }

}