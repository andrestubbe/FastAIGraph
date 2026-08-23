package fastaigraph;

import java.util.*;

/**
 * Ultra-Fast in-memory Directed Knowledge and State Graph.
 * Optimized for real-time traversal, sub-graph extraction, and LLM prompt serialization.
 */
public final class KnowledgeGraph {

    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, List<Edge>> outgoingEdges = new HashMap<>();
    private final Map<String, List<Edge>> incomingEdges = new HashMap<>();

    public synchronized KnowledgeGraph addNode(final String id, final String label) {
        return this.addNode(new Node(id, label));
    }

    public synchronized KnowledgeGraph addNode(final Node node) {
        Objects.requireNonNull(node, "node must not be null");
        this.nodes.put(node.id(), node);
        this.outgoingEdges.putIfAbsent(node.id(), new ArrayList<>());
        this.incomingEdges.putIfAbsent(node.id(), new ArrayList<>());
        return this;
    }

    public synchronized KnowledgeGraph addEdge(final String sourceId, final String relation, final String targetId) {
        return this.addEdge(new Edge(sourceId, relation, targetId));
    }

    public synchronized KnowledgeGraph addEdge(final Edge edge) {
        Objects.requireNonNull(edge, "edge must not be null");
        this.nodes.putIfAbsent(edge.sourceId(), new Node(edge.sourceId(), edge.sourceId()));
        this.nodes.putIfAbsent(edge.targetId(), new Node(edge.targetId(), edge.targetId()));

        this.outgoingEdges.computeIfAbsent(edge.sourceId(), k -> new ArrayList<>()).add(edge);
        this.incomingEdges.computeIfAbsent(edge.targetId(), k -> new ArrayList<>()).add(edge);
        return this;
    }

    public synchronized Optional<Node> getNode(final String id) {
        return Optional.ofNullable(this.nodes.get(id));
    }

    public synchronized List<Edge> getOutgoingEdges(final String nodeId) {
        final List<Edge> edges = this.outgoingEdges.get(nodeId);
        return edges != null ? Collections.unmodifiableList(new ArrayList<>(edges)) : Collections.emptyList();
    }

    public synchronized List<Edge> getIncomingEdges(final String nodeId) {
        final List<Edge> edges = this.incomingEdges.get(nodeId);
        return edges != null ? Collections.unmodifiableList(new ArrayList<>(edges)) : Collections.emptyList();
    }

    public synchronized List<Node> getNeighbors(final String nodeId) {
        final List<Edge> outgoing = this.outgoingEdges.getOrDefault(nodeId, Collections.emptyList());
        final List<Node> neighbors = new ArrayList<>(outgoing.size());
        for (final Edge edge : outgoing) {
            final Node target = this.nodes.get(edge.targetId());
            if (target != null) {
                neighbors.add(target);
            }
        }
        return Collections.unmodifiableList(neighbors);
    }

    /**
     * Traverses graph from starting node up to maxDepth and extracts connected sub-graph triples.
     */
    public synchronized List<Edge> traverseSubGraph(final String startNodeId, final int maxDepth) {
        if (!this.nodes.containsKey(startNodeId) || maxDepth <= 0) {
            return Collections.emptyList();
        }

        final Set<String> visitedNodes = new HashSet<>();
        final Set<Edge> collectedEdges = new LinkedHashSet<>();
        final Queue<String> queue = new ArrayDeque<>();

        queue.add(startNodeId);
        visitedNodes.add(startNodeId);

        int currentDepth = 0;
        while (!queue.isEmpty() && currentDepth < maxDepth) {
            final int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                final String curr = queue.poll();
                final List<Edge> edges = this.outgoingEdges.getOrDefault(curr, Collections.emptyList());
                for (final Edge e : edges) {
                    collectedEdges.add(e);
                    if (visitedNodes.add(e.targetId())) {
                        queue.add(e.targetId());
                    }
                }
            }
            currentDepth++;
        }

        return new ArrayList<>(collectedEdges);
    }

    /**
     * Serializes sub-graph triples around a query into a clean, token-efficient text format for LLM Prompts.
     */
    public synchronized String toPromptContext(final String query, final int maxDepth) {
        final List<Edge> matchedEdges = new ArrayList<>();
        final String lowerQuery = query != null ? query.toLowerCase() : "";

        for (final Node node : this.nodes.values()) {
            if (lowerQuery.contains(node.id().toLowerCase()) || lowerQuery.contains(node.label().toLowerCase())) {
                matchedEdges.addAll(this.traverseSubGraph(node.id(), maxDepth));
            }
        }

        if (matchedEdges.isEmpty()) {
            // Fallback: If no direct match, return all top relations
            for (final List<Edge> list : this.outgoingEdges.values()) {
                matchedEdges.addAll(list);
                if (matchedEdges.size() >= 15) break;
            }
        }

        if (matchedEdges.isEmpty()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder("[Knowledge Graph Context]:\n");
        final Set<Edge> unique = new LinkedHashSet<>(matchedEdges);
        for (final Edge edge : unique) {
            final Node src = this.nodes.get(edge.sourceId());
            final Node tgt = this.nodes.get(edge.targetId());
            final String srcLabel = src != null ? src.label() : edge.sourceId();
            final String tgtLabel = tgt != null ? tgt.label() : edge.targetId();
            sb.append("• (").append(srcLabel).append(") -> [").append(edge.relation()).append("] -> (").append(tgtLabel).append(")\n");
        }
        return sb.toString();
    }

    public synchronized int nodeCount() {
        return this.nodes.size();
    }

    public synchronized int edgeCount() {
        int count = 0;
        for (final List<Edge> list : this.outgoingEdges.values()) {
            count += list.size();
        }
        return count;
    }

    public synchronized void clear() {
        this.nodes.clear();
        this.outgoingEdges.clear();
        this.incomingEdges.clear();
    }
}
