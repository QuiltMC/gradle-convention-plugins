plugins {
    `groovy-gradle-plugin` // TODO: we can't use kts until 7.1 because of a bug in gradle
    `maven-publish`
}

group = "org.quiltmc"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:5.11.0")
}
