package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Результат загрузки данных.
 */

public class LoadResult<T> {

    /**
     * Чем закончилась загрузка? (ок или не ок)
     */
    @NonNull
    public final ResultType resultType;

    /**
     * Загруженные данные (в случае, если resultType == OK) или null.
     */
    @Nullable
    public final T data;

    public final int requestType;

    public final int pagesLoaded;

    public LoadResult(@NonNull ResultType resultType, @Nullable T data, int requestType, int pagesLoaded) {
        this.resultType = resultType;
        this.data = data;
        this.requestType = requestType;
        this.pagesLoaded = pagesLoaded;
    }
}
