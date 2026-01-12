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
    
    implementation("org.hibernate:hibernate-core:6.6.0.Final")
    implementation("org.hibernate.orm:hibernate-jcache:6.6.0.Final")
    
    implementation("org.postgresql:postgresql:42.6.0")
    
    compileOnly("jakarta.platform:jakarta.jakartaee-api:11.0.0")
    
    testCompileOnly("org.projectlombok:lombok:1.18.40")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.40")

    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.eclipse.microprofile.config:microprofile-config-api:3.1")

    implementation("io.minio:minio:8.5.2")
    implementation("org.jboss.weld.se:weld-se-core:3.1.6.Final")
    implementation("org.ehcache:ehcache:3.11.1")

    compileOnly("org.jboss.resteasy:resteasy-multipart-provider:7.0.0.Final")
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