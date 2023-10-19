.DEFAULT_GOAL := build-run

run-dist:
	./build/install/app/bin/app

build:
	./gradlew build

run:
	./gradlew run

test:
	./gradlew test

lint:
	./gradlew checkstyleMain checkstyleTest

report:
	./gradlew jacocoTestReport

start:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

build-run: build run

.PHONY: build
