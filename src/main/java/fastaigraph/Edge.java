package fastaigraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a directed, labeled relationship edge between two nodes.
 */
public final class Edge {

    private final String sourceId;
    private final String relation;
    private final String targetId;
    private final Map<String, Object> attributes;

    public Edge(final String sourceId, final String relation, final String targetId) {
        this(sourceId, relation, targetId, Collections.emptyMap());
    }

    public Edge(final String sourceId, final String relation, final String targetId, final Map<String, Object> attributes) {
        this.sourceId = Objects.requireNonNull(sourceId, "sourceId must not be null");
        this.relation = Objects.requireNonNull(relation, "relation must not be null");
        this.targetId = Objects.requireNonNull(targetId, "targetId must not be null");
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
    }

    public String sourceId() {
        return this.sourceId;
    }

    public String relation() {
        return this.relation;
    }

    public String targetId() {
        return this.targetId;
    }

    public Map<String, Object> attributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Edge edge = (Edge) o;
        return Objects.equals(this.sourceId, edge.sourceId) &&
               Objects.equals(this.relation, edge.relation) &&
               Objects.equals(this.targetId, edge.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sourceId, this.relation, this.targetId);
    }

    @Override
    public String toString() {
        return "(" + this.sourceId + ") -[" + this.relation + "]-> (" + this.targetId + ")";
    }
}
