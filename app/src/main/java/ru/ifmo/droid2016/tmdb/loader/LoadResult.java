package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;

public class LoadResult<T> {

    @NonNull
    public final ResultType resultType;

    @NonNull
    public final T data;

    public final int page;

    LoadResult(@NonNull ResultType resultType, @NonNull T data, int page) {
        this.resultType = resultType;
        this.data = data;
        this.page = page;
    }
}
