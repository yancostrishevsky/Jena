FROM eclipse-temurin:21-jdk

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        maven \
        git \
        curl \
        wget \
        ca-certificates \
        bash \
        docker.io \
        docker-compose \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /usr/local/lib/docker/cli-plugins \
    && printf '%s\n' \
      '#!/usr/bin/env sh' \
      'if [ "$1" = "docker-cli-plugin-metadata" ]; then' \
      '  echo "{\"SchemaVersion\":\"0.1.0\",\"Vendor\":\"Docker Compose\",\"Version\":\"v1\",\"ShortDescription\":\"Docker Compose (v1 wrapper)\",\"URL\":\"https://docs.docker.com/compose/\"}"' \
      '  exit 0' \
      'fi' \
      'exec /usr/bin/docker-compose "$@"' \
      > /usr/local/lib/docker/cli-plugins/docker-compose \
    && chmod +x /usr/local/lib/docker/cli-plugins/docker-compose

WORKDIR /workspace
