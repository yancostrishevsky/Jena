.PHONY: dev cli run test build

dev:
	docker compose run --rm dev bash

cli:
	docker compose run --rm cli

run:
	docker compose up --build app

test:
	mvn -q test

build:
	mvn -q -DskipTests package
