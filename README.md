# EUID — Evolutionary Unique Identifier

EUID is a high-performance, time-ordered UUID (v8) generator designed for distributed systems and database-friendly indexing.

It combines monotonic time ordering with structured topology awareness — while remaining fully RFC 4122 compliant.


---------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------
------------------------------------------------------------------------------------

# ✨ Features

✅ Time-ordered (monotonic)

✅ 48-bit millisecond timestamp

✅ Region / shard / node topology encoding

✅ 48-bit per-node sequence counter

✅ RFC 4122 compliant (UUID v8, variant 2)

✅ Base58 encoding support

✅ Full decode capability

✅ Zero external dependencies

✅ Designed for distributed systems

---------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------

🚀 Why EUID?

Comparison with UUID v4 and UUID v7

* UUID v4

- Fully random

- Not time-ordered

- Poor database index locality

- No infrastructure awareness

* UUID v7

- Time-ordered

- 48-bit timestamp

- Random entropy bits

- Improved database performance

* EUID

- Time-ordered (48-bit timestamp)

- Structured topology encoding (region / shard / node)

- Deterministic monotonic sequence per node

- Fully reversible and decodable

- SQL-index friendly

- Infrastructure-aware identifier design

EUID does not replace UUID v7 — it provides an alternative approach focused on
topology encoding and deterministic structure rather than randomness.


# 🧠 Bit Layout

EUID uses a structured 128-bit layout:

| 48-bit Timestamp | 4-bit Version | 6-bit Region    | 6-bit Shard |
| ---------------- | ------------- | --------------- | ----------- |
| 2-bit Variant    | 14-bit Node   | 48-bit Sequence |             |

# Field Description

- Timestamp — Epoch milliseconds

- Region / Shard / Node — Distributed topology identifiers

- Sequence — Monotonic counter per node

- Version — UUID v8

- Variant — RFC 4122

# 🏗 Architecture Philosophy

EUID is designed with infrastructure principles in mind:

- Deterministic structure over randomness

- Database-friendly ordering

- Distributed topology awareness

- Lexicographic ordering matches time ordering

- Zero runtime dependencies

- Minimal allocation strategy

- RFC-compatible UUID layout

- Explicit decode capability (no black-box IDs)

The goal is not just uniqueness — but operational intelligence.


📦 Installation

Maven

    <dependency>
        <groupId>io.github.louis-franck-moussima</groupId>
        <artifactId>euid-core</artifactId>
        <version>0.1.0</version>
    </dependency>

⚡ Quick Example

    import io.github.louisfranckmoussima.euid.core.*;

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


🧪 Testing

Unit tests cover:

UUID generation correctness

Decode accuracy

Base58 round-trip validation

Constructor validation

Ordering guarantees

Run tests:

mvn test


📄 License

Licensed under the Apache License, Version 2.0.

See the LICENSE file for details.


👤 Author

Louis-Franck Moussima
GitHub: https://github.com/louis-franck-moussima