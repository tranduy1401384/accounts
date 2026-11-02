# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

`accounts` is one of three independent Spring Boot 4.1.2 / Java 21 Maven projects under `SupperApp/` (siblings: `profile`, `edge`; there is no parent/aggregator pom). `edge` is the Spring Cloud Gateway in front of the services. No port, security or datasource is configured yet (default port 8080 would clash with Keycloak from `../../setup/docker-compose.yml`; the intended port in `../../setup/note.md` is 8081).

## Architecture

Layered example CRUD under `com.duytv4.accounts`, REST at `/api/accounts`:

- `controller/AccountController` -> `service/AccountService` -> in-memory `ConcurrentHashMap` + `AtomicLong` id sequence (no repository/DB; replace with one once a datasource exists).
- `model/Account` is a Lombok `@Data`/`@Builder` POJO (`id, accountNumber, ownerName, balance, status`) used directly as the request/response body; there is no DTO layer or validation. `create` defaults `balance` to 0 and `status` to `"ACTIVE"`.
- Errors: `AccountNotFoundException` is annotated `@ResponseStatus(NOT_FOUND)`; there is no `@ControllerAdvice`.
- The only test is still the context-load test; nothing covers the CRUD layer.

Dependencies: `spring-boot-starter-webmvc` (Spring MVC, not WebFlux), Lombok (optional), `spring-boot-starter-webmvc-test`. Note the Boot 4 starter names (`webmvc`, `webmvc-test`), which differ from Boot 3's `web`/`test`.

## Commands

Run from this directory using the wrapper:

```
./mvnw spring-boot:run
./mvnw test
./mvnw test -Dtest=AccountsApplicationTests                 # single class
./mvnw test -Dtest=AccountsApplicationTests#contextLoads    # single method
./mvnw package
```

No lint/format tooling is configured.

## Build notes

Lombok is wired in through explicit `annotationProcessorPaths` on the `default-compile` and `default-testCompile` executions in `pom.xml`. Keep both if editing the compiler plugin, or Lombok annotations will stop being processed.
