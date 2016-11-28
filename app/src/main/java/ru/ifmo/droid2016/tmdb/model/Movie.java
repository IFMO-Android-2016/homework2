package ru.ifmo.droid2016.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Информация о фильме, полученная из The Movie DB API
 */

public class Movie implements Parcelable {

    /**
     * Path изображения постера фильма. Как из Path получить URL, описано здесь:
     *
     * https://developers.themoviedb.org/3/getting-started/images
     *
     * В рамках ДЗ можно не выполнять отдельный запрос /configuration, а использовать
     * базовый URL для картинок: http://image.tmdb.org/t/p/ и
     */
    public final @NonNull String posterPath;

    /**
     * Название фильма на языке оригинала.
     */
    public final @NonNull String originalTitle;

    /**
     * Описание фильма на языке пользователя.
     */
    public final @Nullable String overviewText;
    public final String BaseURL = "https://image.tmdb.org/t/p/w500";

    /**
     * Название фильма на языке пользователя.
     */
    public final @Nullable String localizedTitle;

    protected Movie(Parcel in) {
                posterPath = in.readString();
                originalTitle = in.readString();
                overviewText = in.readString();
                localizedTitle = in.readString();
            }

    @Override public int describeContents(){
        return(0);
    }
    @Override
        public void writeToParcel(Parcel dest, int something) {
           dest.writeString(posterPath);
          dest.writeString(originalTitle);
          dest.writeString(overviewText);
         dest.writeString(localizedTitle);
    }
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
                @Override
                public Movie createFromParcel(Parcel in) {
                        return new Movie(in);
                    }
        
                        @Override
                public Movie[] newArray(int size) {
                        return new Movie[size];
                    }
            };
    public Movie(String posterPath,
                 String originalTitle,
                 String overviewText,
                 String localizedTitle) {
        this.posterPath = BaseURL+posterPath;
        this.originalTitle = originalTitle;
        this.overviewText = overviewText;
        this.localizedTitle = localizedTitle;
    }
}
