plugins {
    war
    java
    checkstyle
}

group = "com.is"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    
    compileOnly("org.projectlombok:lombok:1.18.40")
    annotationProcessor("org.projectlombok:lombok:1.18.40")
    
    implementation("org.eclipse.persistence:eclipselink:4.0.8")
    
    implementation("org.postgresql:postgresql:42.6.0")
    
    compileOnly("jakarta.platform:jakarta.jakartaee-api:11.0.0")
    
    testCompileOnly("org.projectlombok:lombok:1.18.40")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.40")
}

tasks.war {
    archiveFileName.set("lab1.war")
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