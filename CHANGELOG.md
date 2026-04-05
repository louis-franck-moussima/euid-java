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

## [0.2.0] - 2026-04-05

### Added
- Introduced `FastEuidGenerator` for single-threaded or thread-confined generation
- Introduced `ConcurrentEuidGenerator` for shared concurrent generation
- Introduced `EuidGenerator` as the common generator abstraction
- Introduced `EuidGenerators` factory methods for creating generator implementations
- Added support for configurable block size in `ConcurrentEuidGenerator`
- Added batch generation support compatible with the new generator abstraction
- Added basic tests covering:
  - UUID generation
  - decode validation
  - Base58 round-trip
  - parameter validation
  - custom block size validation
- Added internal benchmark coverage for:
  - single-thread generation
  - shared concurrent generation
  - thread-confined generation
  - duplicate safety
  - ordering
  - Base58
  - batch generation

### Changed
- Refactored generator architecture to separate low-overhead and concurrent generation strategies
- Updated batch generators to depend on the `EuidGenerator` abstraction instead of a concrete generator type
- Improved API clarity by distinguishing fast and concurrent generation use cases
- Improved concurrent throughput through block-based sequence reservation
- Improved test coverage for the new generator model

### Performance
- Significantly improved multi-threaded throughput with `ConcurrentEuidGenerator`
- Added a high-performance thread-confined strategy with `FastEuidGenerator`

### Notes
- `0.2.0` is the recommended version for new users
- `0.1.0` remains available on Maven Central
- API may continue to evolve before `1.0.0`

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