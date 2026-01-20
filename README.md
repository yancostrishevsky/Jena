# Jena LAB6 - Docker/DevContainer project

This project provides a Java (Maven) application using Apache Jena for:
- RDF/OWL I/O
- Graph browsing
- SPARQL queries
- Rule-based reasoning

## Quick start (DevContainer)
- Open the repo in VS Code and reopen in container.
- The container runs `mvn -q -DskipTests package` after creation.

## Docker Compose
- Dev shell:
  - `docker compose run --rm dev bash`
- Jena CLI example (validate TTL):
  - `docker compose run --rm cli riot --validate /data/wardrobe.ttl`
- Run app container (default command `io`):
  - `docker compose up --build app`
- Override command (example):
  - `docker compose run --rm app browse`

## Local Maven
- Build: `mvn -q -DskipTests package`
- Test: `mvn -q test`
- Run jar:
  - `java -jar target/jena-lab6.jar io`
  - `java -jar target/jena-lab6.jar browse`
  - `java -jar target/jena-lab6.jar sparql --query queries/red-clothes.rq`
  - `java -jar target/jena-lab6.jar reason --rules rules/wardrobe.rules`

## App CLI
```
java -jar target/jena-lab6.jar [options] <command>

Commands:
  io       Load RDF and write to out/out.ttl
  browse   Print basic graph stats and sample triples
  sparql   Run SPARQL query (requires --query)
  reason   Run rule-based reasoning (requires --rules)

Options:
  --input, -i   Input file (default: data/wardrobe.ttl)
  --format, -f  Output format for io: TTL|RDFXML|JSONLD (default: TTL)
  --query       SPARQL query file path
  --rules       Jena rules file path
```

## Demo
- `./demo.sh`

## Make targets
- `make dev`
- `make cli`
- `make run`
- `make test`
- `make build`
