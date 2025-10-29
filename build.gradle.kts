plugins {
    id("java")
    alias(libs.plugins.quarkus)
}

group = "xq.yiesko"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(libs.quarkus.bom))
    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.resteasy.reactive)
    implementation(libs.quarkus.resteasy.reactive.qute)
    implementation(libs.quarkus.qute)
    implementation(libs.quarkus.hibernate.orm.panache)
    implementation(libs.quarkus.hibernate.validator)
    implementation(libs.quarkus.rest.jackson)
    implementation(libs.quarkus.jdbc.mariadb)
    implementation(libs.quarkus.smallrye.openapi)
    implementation(libs.quarkus.smallrye.health)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(platform(libs.quarkus.bom))
    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.rest.assured)
    testImplementation(libs.quarkus.jdbc.h2)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}