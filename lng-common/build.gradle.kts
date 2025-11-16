dependencies {
    implementation(project(":api"))
    implementation(libs.leyneck)
    compileOnly(libs.jetanno)

    implementation(libs.bundles.adventure)
}

tasks.named("sourcesJar") {
    mustRunAfter(":api:jar")
}
