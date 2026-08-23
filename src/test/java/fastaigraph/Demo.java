package fastaigraph;

import java.util.List;

/**
 * Interactive Demo showcasing FastAIGraph entity linking, graph traversal, and LLM context extraction.
 */
public class Demo {

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("     ⚡ FastAIGraph — Ultra-Fast In-Memory Knowledge Graph Demo          ");
        System.out.println("=========================================================================\n");

        final KnowledgeGraph graph = new KnowledgeGraph();

        // 1. Define entities (Nodes)
        graph.addNode("user", "Developer")
             .addNode("fastai", "FastAI Engine")
             .addNode("fastaimemory", "FastAIMemory Module")
             .addNode("fastaigraph", "FastAIGraph Module")
             .addNode("java17", "Java 17+")
             .addNode("zero_deps", "Zero Dependencies")
             .addNode("jmh", "JMH Microbenchmarks");

        // 2. Define relationships (Edges)
        graph.addEdge("user", "prefers", "java17")
             .addEdge("user", "enforces", "zero_deps")
             .addEdge("fastai", "integrates_with", "fastaimemory")
             .addEdge("fastai", "queries", "fastaigraph")
             .addEdge("fastaimemory", "benchmarked_with", "jmh")
             .addEdge("fastaigraph", "targets", "zero_deps");

        System.out.println("📊 Graph Statistics:");
        System.out.println("   • Total Nodes: " + graph.nodeCount());
        System.out.println("   • Total Edges: " + graph.edgeCount());
        System.out.println();

        // 3. Multi-Hop Traversal
        System.out.println("🔍 Multi-Hop Subgraph Traversal from 'user' (Depth = 2):");
        final List<Edge> subGraph = graph.traverseSubGraph("user", 2);
        for (final Edge e : subGraph) {
            System.out.println("   " + e);
        }
        System.out.println();

        // 4. Prompt Context Serialization for LLMs
        System.out.println("📝 Serialized Prompt Context for Query 'fastai':");
        final String promptContext = graph.toPromptContext("fastai", 2);
        System.out.println(promptContext);

        System.out.println("✅ FastAIGraph initialized and ready for production pipelines.");
    }
}
