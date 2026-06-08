workspace(name = "voyage_mart")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# rules_jvm_external
http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-6.2",
    url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/6.2/rules_jvm_external-6.2.tar.gz",
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")
rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")
rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")
maven_install(
    name = "maven",
    artifacts = [
        # Spring Boot core dependencies required by rules_spring
        "org.springframework.boot:spring-boot:3.2.5",
        "org.springframework.boot:spring-boot-actuator:3.2.5",
        "org.springframework.boot:spring-boot-actuator-autoconfigure:3.2.5",
        "org.springframework.boot:spring-boot-autoconfigure:3.2.5",
        "org.springframework.boot:spring-boot-loader:3.2.5",
        "org.springframework.boot:spring-boot-starter:3.2.5",
        "org.springframework.boot:spring-boot-starter-logging:3.2.5",
        "org.springframework:spring-aop:6.1.6",
        "org.springframework:spring-beans:6.1.6",
        "org.springframework:spring-context:6.1.6",
        "org.springframework:spring-core:6.1.6",
        "org.springframework:spring-expression:6.1.6",
        "org.springframework:spring-web:6.1.6",

        # Additional Spring Boot starters
        "org.springframework.boot:spring-boot-starter-web:3.2.5",
        "org.springframework.boot:spring-boot-starter-validation:3.2.5",

        # Micrometer Observability (required by Spring Boot 3.x runtime)
        "io.micrometer:micrometer-observation:1.12.5",
        "io.micrometer:micrometer-core:1.12.5",

        # Logging / JSON / Utility
        "com.fasterxml.jackson.core:jackson-databind:2.15.2",
        "com.fasterxml.jackson.core:jackson-core:2.15.2",
        "com.fasterxml.jackson.core:jackson-annotations:2.15.2",
        
        # Temporal Java SDK (grpc transitives will resolve automatically)
        "io.temporal:temporal-sdk:1.24.0",
        "io.temporal:temporal-serviceclient:1.24.0",
        
        # Logging
        "org.slf4j:slf4j-api:2.0.13",
        
        # Testing
        "org.springframework.boot:spring-boot-starter-test:3.2.5",
        "org.junit.jupiter:junit-jupiter-api:5.10.2",
        "org.junit.jupiter:junit-jupiter-engine:5.10.2",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
    fetch_sources = True,
)

# rules_spring
http_archive(
    name = "rules_spring",
    strip_prefix = "rules_spring-2.6.3",
    url = "https://github.com/salesforce/rules_spring/archive/refs/tags/2.6.3.tar.gz",
)

# rules_pkg
http_archive(
    name = "rules_pkg",
    url = "https://github.com/bazelbuild/rules_pkg/releases/download/0.9.1/rules_pkg-0.9.1.tar.gz",
)

load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")
rules_pkg_dependencies()
