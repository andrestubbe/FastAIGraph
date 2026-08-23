package fastaigraph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a discrete entity, concept, or state node in the knowledge graph.
 */
public final class Node {

    private final String id;
    private final String label;
    private final Map<String, Object> attributes;

    public Node(final String id, final String label) {
        this(id, label, Collections.emptyMap());
    }

    public Node(final String id, final String label, final Map<String, Object> attributes) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.label = label != null ? label : id;
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
    }

    public String id() {
        return this.id;
    }

    public String label() {
        return this.label;
    }

    public Map<String, Object> attributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    public Object get(final String key) {
        return this.attributes.get(key);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Node node = (Node) o;
        return Objects.equals(this.id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Node(" + this.id + " : '" + this.label + "')";
    }
}
