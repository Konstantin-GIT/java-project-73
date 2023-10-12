plugins {
	java
	application
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"

}

application { mainClass.set("hexlet.code.AppApplication") }

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_20
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")


}

tasks.withType<Test> {
	useJUnitPlatform()
}
