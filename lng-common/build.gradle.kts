dependencies {
    implementation(libs.leyneck)
    compileOnly(libs.jetanno)

    compileOnly(libs.bundles.adventure)
    implementation(project(":api"))
}

tasks.named("sourcesJar") {
    mustRunAfter(":api:jar")
}
