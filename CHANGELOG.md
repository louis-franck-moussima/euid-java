# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project aims to follow Semantic Versioning.

## [Unreleased]

### Planned
- JMH benchmarks
- Spring Boot integration
- Additional examples and documentation
- More integration and adoption guides

## [0.1.0] - 2026-04-03

### Added
- First public release of **EUID**
- UUID v8-compatible structured identifier generation
- Time-ordered identifier generation
- Embedded topology metadata:
    - `region`
    - `shard`
    - `node`
- Deterministic sequence component
- Decode support for inspecting identifier structure
- Base58 encoding support
- Java 17+ support
- Zero external dependencies

### Notes
- Initial Maven Central publication of `euid-core`
- Early-stage API, subject to refinement before `1.0.0`