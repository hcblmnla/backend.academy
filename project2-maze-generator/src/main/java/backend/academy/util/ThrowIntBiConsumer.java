package backend.academy.util;

/**
 * Represents an operation that accepts two input arguments and returns no result
 * or throws an exception.
 *
 * @param <T> checked exception type
 * @author alnmlbch
 * @see java.util.function.BiConsumer
 */
@PureFunctional
@FunctionalInterface
public interface ThrowIntBiConsumer<T extends Exception> {

    /**
     * Functional method.
     *
     * @see java.util.function.BiConsumer#accept(Object, Object)
     */
    void accept(int left, int right) throws T;
}
