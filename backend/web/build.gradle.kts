plugins {
    war
}

dependencies {
    compileOnly("jakarta.platform:jakarta.jakartaee-api:11.0.0")
    
    implementation("org.eclipse.persistence:eclipselink:4.0.8")
    implementation("org.postgresql:postgresql:42.6.0")
}

tasks.war {
    archiveFileName.set("lab1.war")
}