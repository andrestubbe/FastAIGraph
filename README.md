# FastAIGraph 0.1.0 — Ultra-Fast In-Memory Knowledge Graph Engine for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastAIGraph/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastAIGraph)

---

**💡 Extremely lightweight, zero-allocation in-memory knowledge and entity-relation graph for the FastJava AI Ecosystem.**

FastAIGraph is a **pure Java in-memory graph engine** built to structure, link, and traverse entities, code symbols, and facts. It replaces flat text retrieval with **structured multi-hop relationship traversal** and produces token-efficient serialized sub-graphs for LLM prompt augmentation.

[![FastAIGraph Showcase](docs/screenshot.png)](docs/screenshot.png)

---

## Quick Start

`java
import fastaigraph.KnowledgeGraph;
import fastaigraph.Edge;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        // 1. Initialize thread-safe Knowledge Graph
        KnowledgeGraph graph = new KnowledgeGraph();

        // 2. Define Entities and Relationships
        graph.addNode("user", "Developer")
             .addNode("java17", "Java 17+")
             .addNode("fastai", "FastAI Engine")
             .addEdge("user", "prefers", "java17")
             .addEdge("fastai", "written_in", "java17");

        // 3. Multi-Hop Subgraph Traversal
        List<Edge> subGraph = graph.traverseSubGraph("user", 2);

        // 4. Extract token-efficient context for LLM prompt injection
        String promptContext = graph.toPromptContext("fastai", 2);
        System.out.println(promptContext);
    }
}
`

---

## Table of Contents

- [Why FastAIGraph?](#why-fastaigraph)
- [Quick Start](#quick-start)
- [Key Features](#key-features)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Installation](#installation)
- [API Reference](#api-reference)
- [Technical Examples & Demos](#technical-examples--demos)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastAIGraph?

Traditional RAG and vector searches only retrieve disconnected text snippets without understanding structured hierarchies or transitive dependencies (e.g. Class A -> calls -> Method B -> in Module C).

FastAIGraph solves this by providing:

- **Explicit Relational Truth** — Triples (Node -> Relation -> Node) eliminate LLM relationship hallucinations.
- **Multi-Hop Traversal** — Traverse deep graph neighborhoods in microseconds (< 2 µs per query).
- **Sub-Graph Prompt Serializer** — Formats extracted subgraphs into compact, human- and LLM-readable Markdown context.
- **Zero External Dependencies** — Built with 100% pure Java 17 primitives with zero Neo4j/TinkerPop/database overhead.

---

## Key Features

- **🕸️ High-Speed In-Memory Graph** — Sub-microsecond Node and Edge index lookups with concurrent thread safety.
- **🔍 Multi-Hop BFS Neighborhood Extraction** — Traverse connected entity boundaries up to depth $.
- **📄 Prompt Context Generator** — Direct serialization to LLM-ready context blocks (	oPromptContext()).
- **📦 Ultra-Small Footprint** — ~15KB compiled JAR with zero GC pressure on hot lookup paths.

---

## Performance Benchmarks

FastAIGraph is rigorously profiled using **JMH** to guarantee zero-overhead graph traversal:

| Metric / Hot-Path Operation | Score (ops/ms) | Ops per Second |
|-----------------------------|----------------|----------------|
| **Multi-Hop SubGraph Traversal** | ~806 ops/ms  | > 806,000 ops/sec |
| **Prompt Context Extraction**    | ~128 ops/ms  | > 128,000 ops/sec |

*Measured on Windows 11, Intel Core i5-1135G7 (Surface Pro 8), JDK 21.0.12. Measures full 3-hop BFS neighbor exploration and context generation.*

### Framework Comparison

FastAIGraph is **zero-dependency** and **in-process** for instant knowledge operations:

| Metric              | Neo4j Java Driver | Apache TinkerPop | FastAIGraph   |
|---------------------|-------------------|------------------|---------------|
| **Dependencies**    | 15+               | 25+              | **0**         |
| **JAR Size**        | ~12MB             | ~20MB            | **~15KB**     |
| **Startup Time**    | 2-5s              | 3-8s             | **<5ms**      |
| **Memory Overhead** | High (JVM + DB)   | High             | **Minimal**   |
| **Learning Curve**  | Hours             | Hours            | **2 minutes** |

---

## API Quick Reference

| Method / Class | Return Type | Description |
|----------------|-------------|-------------|
| graph.addNode(id, label) | KnowledgeGraph | Inserts or updates an entity node. |
| graph.addEdge(src, rel, tgt) | KnowledgeGraph | Links two nodes with a labeled directed relationship. |
| graph.traverseSubGraph(startId, depth) | List<Edge> | Traverses multi-hop sub-graph edges up to max depth. |
| graph.toPromptContext(query, depth) | String | Serializes relevant graph neighborhood into LLM prompt text. |
| graph.getNeighbors(nodeId) | List<Node> | Returns direct adjacent neighbor nodes. |

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependency to your pom.xml:

`xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
<!-- FastAIGraph Library -->
<dependency>
    <groupId>com.github.andrestubbe</groupId>
    <artifactId>FastAIGraph</artifactId>
    <version>0.1.0</version>
</dependency>
</dependencies>
`

---

## API Reference

### Graph Construction

`java
KnowledgeGraph graph = new KnowledgeGraph();
graph.addNode("user", "Developer");
graph.addNode("fastjava", "FastJava Ecosystem");
graph.addEdge("user", "maintains", "fastjava");
`

### Context Extraction for AI Prompts

`java
// Extracts related facts and injects directly into AI request
String graphContext = graph.toPromptContext("fastjava", 2);
AI ai = FastAI.connect("auto:free");
ai.stream(graphContext + "\nUser Question: What is FastJava?", System.out::print);
`

---

## Technical Examples & Demos

| Case | Java Example | Launcher | Description |
|---|---|---|---|
| **Knowledge Graph Demo** | [Demo.java](src/test/java/fastaigraph/Demo.java) | un-demo.bat | Interactive demo showcasing entity creation, multi-hop traversal, and prompt formatting. |
| **JMH Microbenchmarks** | [FastAIGraphBenchmark.java](examples/Benchmark/src/main/java/fastaigraph/FastAIGraphBenchmark.java) | un-benchmark.bat | JMH throughput benchmark for graph traversal and prompt serialization. |

---

## Platform Support

| Platform      | Status            |
|---------------|-------------------|
| Windows 10/11 | ✅ Fully Supported |
| Linux         | 🚧 Planned        |
| macOS         | 🚧 Planned        |

---

## License

MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastAI](https://github.com/andrestubbe/FastAI) — Unified AI client interface for Java
- [FastAIMemory](https://github.com/andrestubbe/FastAIMemory) — Unified conversation history and memory orchestration
- [FastAIRag](https://github.com/andrestubbe/FastAIRag) — Ultra-fast document chunking and vector retrieval
- [FastCore](https://github.com/andrestubbe/FastCore) — Unified JNI loader and platform abstraction

---

**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*
