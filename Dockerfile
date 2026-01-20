FROM eclipse-temurin:21-jdk-jammy

ARG JENA_VERSION=5.6.0
WORKDIR /opt

RUN apt-get update && apt-get install -y --no-install-recommends wget ca-certificates \
 && rm -rf /var/lib/apt/lists/*

RUN wget -q https://downloads.apache.org/jena/binaries/apache-jena-${JENA_VERSION}.tar.gz \
 && tar -xzf apache-jena-${JENA_VERSION}.tar.gz \
 && rm apache-jena-${JENA_VERSION}.tar.gz

ENV JENA_HOME=/opt/apache-jena-${JENA_VERSION}
ENV PATH="${JENA_HOME}/bin:${PATH}"

WORKDIR /work
CMD ["bash"]
