buildscript {
    allprojects {
        extra.apply {
            set("compose_version", "1.5.3")
            set("redux_kotlin", "0.6.0")
            set("ktor", "1.5.0")
        }
    }
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }

    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.10")

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}