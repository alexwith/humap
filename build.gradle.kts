plugins {
    id("java")
}

subprojects {
    apply(plugin = "java")

    group = "com.github.alexwith.humap"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.mongodb:mongodb-driver-sync:4.7.2")
        implementation("net.bytebuddy:byte-buddy:1.14.11")
        implementation("net.bytebuddy:byte-buddy-agent:1.14.11")
        implementation("jakarta.annotation:jakarta.annotation-api:3.0.0-M1")

        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
