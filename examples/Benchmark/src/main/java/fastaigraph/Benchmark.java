package fastaigraph;

import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JMH Microbenchmark — FastAIGraph sub-graph traversal and prompt context extraction.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = {"-server", "-XX:+UseG1GC", "-Xms256m", "-Xmx256m"})
public class Benchmark {

    private KnowledgeGraph graph;

    @Setup(Level.Trial)
    public void setup() {
        this.graph = new KnowledgeGraph();
        for (int i = 0; i < 50; i++) {
            this.graph.addNode("node_" + i, "Concept " + i);
            if (i > 0) {
                this.graph.addEdge("node_" + (i - 1), "relates_to", "node_" + i);
                this.graph.addEdge("node_" + (i / 2), "parent_of", "node_" + i);
            }
        }
    }

    @Benchmark
    public List<Edge> benchmarkSubGraphTraversal() {
        return this.graph.traverseSubGraph("node_0", 3);
    }

    @Benchmark
    public String benchmarkPromptContextExtraction() {
        return this.graph.toPromptContext("Concept 10", 2);
    }
}
