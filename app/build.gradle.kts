plugins {
	java
	application
	id ("checkstyle")
	id ("jacoco")
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"

}

application { mainClass.set("hexlet.code.AppApplication") }


checkstyle {
	toolVersion = "10.12.1"

}

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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2:2.1.214")
	implementation("org.postgresql:postgresql:42.6.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")



}

tasks.register("stage") {
	dependsOn("installDist")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
	}
}


