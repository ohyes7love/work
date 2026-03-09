# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`restSeqApi` is a Spring Boot 3.0.4 REST API server for distributed sequence number (채번) and GUID generation. It uses Redis with Redisson for distributed locking to ensure uniqueness under concurrent load. The server runs on port **9091**.

## Build & Run Commands

```bash
# Build
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.rok.seq.Test"

# OWASP dependency vulnerability check
./gradlew dependencyCheckAnalyze
```

Requirements: Java 17, Gradle 7.6.1 (via wrapper), Redis running at `localhost:6379`, Kafka at `localhost:9092`.

A Windows Redis binary is included at `work/Redis-x64-3.0.504/redis-server.exe` for local development.

## Architecture

### Package Structure (`com.rok.seq`)
- `controller/` — REST controllers and `dto/` for request/response objects
- `service/` — Business logic and `dto/` for internal data transfer
- `redis/` — Redis and Redisson configuration
- `constant/SeqApiConstant.java` — Application-wide constants (lock key, max sequence, GUID character set)
- `utils/` — `DateUtils`, `StringUtils`

### Current State (Important)
Most of the core code is **commented out**. Only `HelloController` (`GET /hello`, `GET /hello2`) is currently active. The commented-out classes represent the intended design:

- **`SeqApiController`** (`/seqApi`): `POST /getGuid`, `GET /getSeq`, `GET /getCurrentSeq`
- **`TestApiController`** (`/testApi`): `GET /setPreDate`, `GET /setMaxSeq`, `GET /setInit` — for manually manipulating state during testing
- **`GenSeqService`**: Sequence generation with Redisson distributed lock (`seqLock` key, 10s timeout); stores `SequenceStateDto` (date + sequence) in Redis under key `seq`; resets to 0 daily; max sequence is `9999999999`
- **`GenGuidService`**: GUID generation — 30-char string: `yyyyMMddHHmmssSSS(17) + channelCode(3) + nodeNo(2) + instanceNo(2) + random(6)`
- **`RedisConfig`**: Configures `RedisTemplate<String, Object>` with Jackson serializer for `SequenceStateDto`, and `RedissonClient` for distributed locking

### Key Design Pattern
Sequence generation acquires a Redisson distributed lock before reading/incrementing/writing back to Redis, ensuring no duplicate sequences across concurrent requests. The date is stored alongside the sequence so that the counter resets to 1 each new day.

### Infrastructure
- **Redis**: State store for sequence data; Lettuce client for data access, Redisson for distributed locks
- **Kafka**: Configured as consumer/producer (group `myGroup`) but not yet wired into business logic
- **MyBatis + MySQL**: Dependencies present but not yet used in active code
- **JMeter**: Load test plans stored in `work/apache-jmeter-5.5/`

### DTOs
- `GuidInDto`: `sendChlCd` (3-char channel code), `sendSysNodeNo` (int), `sendSysInstNo` (int) — validated with `@Valid`
- `GuidOutDto`: `guid` (30-char string)
- `SeqOutDto`: `sequence` (Long)
- `CurrSeqOutDto`: `sequence` (Long), `date` (String)
- `SequenceStateDto`: Internal Redis storage object with `date` (yyyyMMdd) and `currentSequence` (long)
