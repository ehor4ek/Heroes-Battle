plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(files("libs/heroes_task_lib-1.0-SNAPSHOT.jar"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Copy>("copyJarToProject") {
    dependsOn(tasks.jar)

    from(tasks.jar.get().archiveFile)
    into(project.properties["targetPath"]?.toString() ?: "C:/Project/heroes/jars")
    rename { "obf.jar" }

    doLast {
        println("JAR скопирован в ${project.properties["targetPath"] ?: "C:/Project/heroes/jars"}/obf.jar")
    }
}

//tasks.register<Exec>("runTargetProject") {
//    dependsOn("copyJarToProject")
//
//    workingDir(project.properties["targetPath"]?.toString() ?: "C:/Project/heroes")
//    commandLine("java", "-jar", "Heroes Battle-1.0.0.jar")
//}