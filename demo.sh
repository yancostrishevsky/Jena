#!/usr/bin/env bash
set -euo pipefail

echo "== Build =="
mvn -q package

echo "== IO =="
java -jar target/jena-lab6.jar io

echo "== Browse =="
java -jar target/jena-lab6.jar browse

echo "== SPARQL =="
java -jar target/jena-lab6.jar sparql --query queries/red-clothes.rq

echo "== Reasoning =="
java -jar target/jena-lab6.jar reason --rules rules/wardrobe.rules
