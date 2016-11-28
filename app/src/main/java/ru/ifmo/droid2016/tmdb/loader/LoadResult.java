package ru.ifmo.droid2016.tmdb.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LoadResult<T> {

    @NonNull
    public final ResultType resultType;

    @Nullable
    public final T data;
    public final int page;

    public LoadResult(@NonNull ResultType resultType, @Nullable T data, int page) {
        this.resultType = resultType;
        this.data = data;
        this.page = page;
    }
}
