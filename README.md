# EUID — Topology-Aware UUID v8 for Java

![Maven Central](https://img.shields.io/maven-central/v/io.github.louis-franck-moussima/euid-core)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)
![Java](https://img.shields.io/badge/java-17+-orange)

**EUID** is a Java library for generating **sortable, decodable, infrastructure-aware UUID v8 identifiers**.

It is designed for distributed systems that need more than raw uniqueness:
- **time-ordered IDs** for better database locality
- **embedded topology metadata** (`region`, `shard`, `node`)
- **deterministic per-node sequencing**
- **RFC 4122 / UUID v8-compatible layout**
- **human-friendlier Base58 encoding**
- **full decode support** for observability and debugging

If you need identifiers that are not just unique, but also **operationally meaningful**, EUID gives you a structured alternative to purely random UUIDs.

---

## Why EUID?

Traditional identifiers solve uniqueness, but they do not always help with:
- ordered inserts in databases
- infrastructure traceability
- decoding where an ID came from
- correlating events across distributed nodes

EUID is built for those cases.

### In one sentence

**EUID = sortable UUID v8 + topology metadata + deterministic sequencing**

---

## When to use EUID

EUID is a good fit when you are building:

- distributed backends
- event-driven systems
- sharded or multi-region services
- systems that benefit from time-ordered IDs
- systems where IDs should expose operational context
- services that need to decode identifiers for debugging or tracing

### When not to use EUID

EUID may *not* be the right choice if:

- you only need a simple random ID
- you explicitly do not want IDs to reveal topology structure
- UUID v4 or UUID v7 already fully solves your problem
- you prefer randomness over structured metadata

---

## Main features

- UUID v8-compatible 128-bit layout
- 48-bit millisecond timestamp
- embedded `region`, `shard`, and `node`
- 48-bit sequence counter
- monotonic per-node generation
- lexicographically sortable by time
- full decode support
- Base58 encoding support
- zero external dependencies
- Java 17+

---

## Installation

### Maven

    ```xml
    <dependency>
        <groupId>io.github.louis-franck-moussima</groupId>
        <artifactId>euid-core</artifactId>
        <version>0.1.0</version>
    </dependency>

---------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------

# ✨ Quick start

    import io.github.louisfranckmoussima.euid.core.DecodedEuid;
    import io.github.louisfranckmoussima.euid.core.EuidBase58Codec;
    import io.github.louisfranckmoussima.euid.core.EuidDecoder;
    import io.github.louisfranckmoussima.euid.core.EuidGenerator;

    import java.util.UUID;

    public class Main {
        public static void main(String[] args) {
        EuidGenerator generator = new EuidGenerator(1, 1, 1);

        UUID id = generator.generate();
        System.out.println("EUID: " + id);

        String base58 = EuidBase58Codec.encode(id);
        System.out.println("Base58: " + base58);

        DecodedEuid decoded = EuidDecoder.decode(id);
        System.out.println("Timestamp: " + decoded.getInstant());
        System.out.println("Region: " + decoded.getRegion());
        System.out.println("Shard: " + decoded.getShard());
        System.out.println("Node: " + decoded.getNode());
        System.out.println("Sequence: " + decoded.getSequence());
        }
    }

---------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------

# Example output

     EUID: 019caad2-a445-8083-8005-000000000011
 
     Base58: 1CYctoRWaz5VGXwscmiovG
 
     Timestamp: 2026-03-01T19:14:17.285Z
 
     Region: 2
 
     Shard: 3
 
     Node: 5
 
     Sequence: 17

--------------------------------------------------------------------------------------------------------

# 🧠 Bit Layout

EUID uses a structured 128-bit layout:

| Bits | Field     |
| ---- | --------- |
| 48   | Timestamp |
| 4    | Version   |
| 6    | Region    |
| 6    | Shard     |
| 2    | Variant   |
| 14   | Node      |
| 48   | Sequence  |

# Field Description

- Timestamp: epoch milliseconds

- Version: UUID v8

- Region: logical deployment region

- Shard: shard or partition identifier

- Variant: RFC 4122 variant

- Node: generator node identifier

- Sequence: monotonic per-node counter

---------------------------------------------------------------------------------

# Design philosophy

EUID is intentionally structured.

It favors:

 - deterministic structure over opaque randomness
 - time ordering over insertion disorder
 - operational observability over black-box IDs
 - decodability over total opacity

That makes it especially useful in systems where identifiers are part of the operational story.


--------------------------------------------------------------------
-----------------------------------------------------------

# Comparison

UUID v4
- random
- widely used
- no time ordering
- no topology awareness
- poor index locality for some database workloads

UUID v7
- time-ordered
- improved database behavior
- still primarily entropy-based
- no embedded infrastructure metadata

EUID
- time-ordered
- topology-aware
- deterministic per-node sequence
- decodable
- designed for distributed infrastructure use cases

EUID does not try to replace every UUID use case.
It offers a structured alternative for systems that benefit from sortable and meaningful identifiers.


------------------------------------------------------------------------

# Decoding example

    UUID id = generator.generate();
    DecodedEuid decoded = EuidDecoder.decode(id);

    System.out.println(decoded.getInstant());
    System.out.println(decoded.getRegion());
    System.out.println(decoded.getShard());
    System.out.println(decoded.getNode());
    System.out.println(decoded.getSequence());


This is useful for:

- tracing where an ID was produced
- debugging routing or sharding behavior
- understanding generation order
- operational analytics

-----------------------------------------------------------------
-------------------------------------------------------------------------

# Base58 support

EUID can be represented in Base58 for more compact and user-friendlier output.

    UUID id = generator.generate();

    String encoded = EuidBase58Codec.encode(id);
    System.out.println(encoded);


Use Base58 when you want:

- shorter string representations
- reduced visual ambiguity
- friendlier copy/paste in logs, URLs, or dashboard

--------------------------------------------------------------------------

# Testing

The project includes tests for:

- UUID generation correctness
- decode accuracy
- Base58 round-trip validation
- constructor validation
- ordering guarantees

Run tests with:

    mvn test

-------------------------------------------------------------------------------
# Performance

Benchmark results are planned.

Future benchmark coverage will include:

- generation throughput
- contention scenarios
- comparison with UUID.randomUUID()
- comparison with UUID v7 implementations
- ordered insertion behavior in databases

-----------------------------------------------------------------------------------

# Roadmap

Planned improvements:

- Spring Boot auto-configuration module
- JMH benchmarks
- more examples and integrations
- additional encodings
- expanded documentation

-------------------------------------------------------------------------

# Versioning

The project is currently in an early stage.

Before 1.0.0, APIs may still evolve as the library matures and real-world feedback is incorporated.

-------------------------------------------------------------------------

# Contributing

Issues, ideas, discussions, and pull requests are welcome.

Areas where feedback is especially valuable:

- API ergonomics
- bit layout evolution
- multi-threaded generation behavior
- benchmark methodology
- Spring / JPA integration
- production use cases

-----------------------------------------------------------------------

# License

Licensed under the Apache License, Version 2.0.

See LICENSE for details.

----------------------------------------------------------------------------

# 👤 Author

Louis Franck Moussima

GitHub: https://github.com/louis-franck-moussima
