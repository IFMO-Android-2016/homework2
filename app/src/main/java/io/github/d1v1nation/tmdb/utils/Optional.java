package io.github.d1v1nation.tmdb.utils;

/**
 * @author d1v1nation (catanaut@gmail.com)
 *         <p>
 *         23.11.16 of movies | io.github.d1v1nation.tmdb.utils
 *
 *         god damn this prehistoric language why do i have to write my own optional
 */
public class Optional<T> {
    private final T value;

    Optional() {
        value = null;
    };
    Optional(T t) {
        value = t;
    }

    public T get() {
        if (value != null)
            return value;
        else
            throw new RuntimeException("Empty optional");
    }

    public T get(T def) {
        if (value != null)
            return value;
        else
            return def;
    }
}
