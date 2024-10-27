package backend.academy.util;

import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;

/**
 * {@link Function} utility class.
 *
 * @author alnmlbch
 */
@PureFunctional
@UtilityClass
public final class Functions {

    /**
     * 2-compositor
     */
    public @NonNull static <T, S, R> Function<T, R> compose(
        final @NonNull Function<? super S, R> g,
        final @NonNull Function<? super T, ? extends S> f
    ) {
        return g.compose(f);
    }

    /**
     * 3-compositor
     */
    public @NonNull static <T, S, R, V> Function<T, V> compose(
        final @NonNull Function<? super R, V> v,
        final @NonNull Function<? super S, ? extends R> g,
        final @NonNull Function<? super T, ? extends S> f
    ) {
        return compose(v, compose(g, f));
    }

    /**
     * 4-compositor
     */
    public @NonNull static <T, S, R, V, U> Function<T, U> compose(
        final @NonNull Function<? super V, U> u,
        final @NonNull Function<? super R, ? extends V> v,
        final @NonNull Function<? super S, ? extends R> g,
        final @NonNull Function<? super T, ? extends S> f
    ) {
        return compose(u, compose(v, g, f));
    }
}
