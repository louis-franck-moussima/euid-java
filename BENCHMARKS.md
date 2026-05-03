# Benchmarks

Official JMH benchmarks for **EUID** are maintained in the separate **[`euid-benchmarks`](https://github.com/louis-franck-moussima/euid-benchmarks)** project.

This benchmark project contains:

- benchmark source code
- runner classes
- JMH benchmark setup
- benchmark execution instructions
- benchmark results and environment details

## Version note

The published benchmark results currently associated with **`euid-core:0.2.0`** were executed from the `euid-benchmarks` project against:

- **Library under test:** `io.github.louis-franck-moussima:euid-core:0.2.0`

## What is benchmarked

The benchmark suite covers:

- raw generation
- multi-thread throughput
- generation followed by `toString()`
- Base58 encoding and decoding

## Where to find the full benchmark report

See the full benchmark repository here:

**[`euid-benchmarks`](https://github.com/louis-franck-moussima/euid-benchmarks)**

## Running benchmarks

Benchmarks can be executed from the packaged JMH jar, for example:

```bash
java -jar target/benchmarks.jar StringAndBase58Benchmark