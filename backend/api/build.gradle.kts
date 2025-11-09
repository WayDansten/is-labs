dependencies {
    implementation(project(":core"))
    implementation(project(":persistence"))
    implementation(project(":web"))
    
    compileOnly("jakarta.platform:jakarta.jakartaee-api:11.0.0")
}