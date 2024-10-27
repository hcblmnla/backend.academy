package backend.academy.util.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NonNull;

/**
 * Classic DSU algorithm.
 *
 * @param <T> elements in set type
 * @author alnmlbch
 */
public final class DisjointSetUnion<T> {

    public final Map<T, T> parent = new HashMap<>();
    private final Map<T, Integer> size = new HashMap<>();

    public DisjointSetUnion(final @NonNull List<T> elements) {
        elements.forEach(el -> {
            parent.put(el, el);
            size.put(el, 1);
        });
    }

    public boolean inSameSet(final @NonNull T v, final @NonNull T u) {
        return leader(v).equals(leader(u));
    }

    public T leader(final @NonNull T v) {
        if (parent.get(v).equals(v)) {
            return v;
        }
        final var p = leader(parent.get(v));
        parent.put(v, p);
        return p;
    }

    public void union(final @NonNull T a, final @NonNull T b) {
        final var la = leader(a);
        final var lb = leader(b);
        if (size.get(la) > size.get(lb)) {
            merge(la, lb);
        } else {
            merge(lb, la);
        }
    }

    private void merge(final @NonNull T a, final @NonNull T b) {
        parent.put(b, a);
        size.merge(a, size.get(b), Integer::sum);
    }
}
