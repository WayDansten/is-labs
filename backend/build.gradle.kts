plugins {
    java
    checkstyle
}

allprojects {
    group = "com.is"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")
    apply(plugin = "checkstyle")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.40")
        annotationProcessor("org.projectlombok:lombok:1.18.40")
        
        testCompileOnly("org.projectlombok:lombok:1.18.40")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.40")
        testImplementation("junit:junit:4.13.2")
    }

    checkstyle {
        config = resources.text.fromFile("checkstyle.xml")
        isShowViolations = true
        isIgnoreFailures = false
    }

    tasks.withType<Checkstyle>().configureEach {
        reports {
            xml.required.set(false)
            html.required.set(false)
        }
    }
}