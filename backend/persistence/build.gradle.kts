dependencies {
    implementation(project(":core"))
    
    implementation("org.eclipse.persistence:eclipselink:4.0.8")
    implementation("org.postgresql:postgresql:42.6.0")
    
    compileOnly("jakarta.platform:jakarta.jakartaee-api:11.0.0")
}