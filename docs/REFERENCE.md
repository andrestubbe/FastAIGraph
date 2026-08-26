# FastAIGraph API Reference

## Core Engine

### `KnowledgeGraph`
High-performance in-memory knowledge graph and multi-hop relation engine.

* `addNode(String id, String label)`: Adds an entity node to the graph.
* `addEdge(String sourceId, String relationship, String targetId)`: Creates a directed relationship between two nodes.
* `getNeighbors(String nodeId)`: Returns direct adjacent neighbor nodes.
* `traverseSubGraph(String startNodeId, int depth)`: Traverses multi-hop relations up to N levels deep.
* `toPromptContext(String queryEntity, int depth)`: Serializes structured sub-graph context into prompt-ready markdown for LLMs.
