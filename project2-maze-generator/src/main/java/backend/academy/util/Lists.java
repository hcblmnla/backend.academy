package backend.academy.util;

import java.util.List;
import java.util.Random;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * {@link List} utility class.
 *
 * @author alnmlbch
 */
@UtilityClass
public final class Lists {

    /**
     * Returns nullable random element.
     */
    public <T> @Nullable T getNullableRandomElement(final @NonNull List<T> list, final @NonNull Random random) {
        return list.isEmpty() ? null : getRandomElement(list, random);
    }

    /**
     * Returns non-null random element but exception can be thrown.
     */
    public <T> @NonNull T getRandomElement(final @NonNull List<T> list, final @NonNull Random random) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        return list.get(random.nextInt(list.size()));
    }
}
