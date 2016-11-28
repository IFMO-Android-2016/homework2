package ru.ifmo.droid2016.tmdb.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import ru.ifmo.droid2016.tmdb.R;
import ru.ifmo.droid2016.tmdb.api.TmdbApi;

/**
 * Информация о фильме, полученная из The Movie DB API
 */

public class Movie implements Parcelable {

    /**
     * ID фильма в каталоге TMDB.
     */
    public final int id;

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
    public final @NonNull String overviewText;

    /**
     * Название фильма на языке пользователя.
     */
    public final @NonNull String localizedTitle;

    /**
     * Рейтинг фильма.
     */
    public final double rating;

    /**
     * Пустой объект-лоадер
     */
    private final boolean empty;

    private boolean error = false;
    private int message = R.string.error;

    public Movie() {
        this.id = 0;
        this.posterPath = "";
        this.originalTitle = "";
        this.overviewText = "";
        this.localizedTitle = "";
        this.rating = 0;
        this.empty = true;
    }

    public Movie(int id,
                 @NonNull String posterPath,
                 @NonNull String originalTitle,
                 @NonNull String overviewText,
                 @NonNull String localizedTitle,
                 double rating) {
        this.id = id;
        this.posterPath = TmdbApi.getImageURI() + posterPath;
        this.originalTitle = originalTitle;
        this.overviewText = overviewText;
        this.localizedTitle = localizedTitle;
        this.rating = rating;
        this.empty = false;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        originalTitle = in.readString();
        overviewText = in.readString();
        localizedTitle = in.readString();
        rating = in.readDouble();
        empty = in.readByte() != 0;
        error = in.readByte() != 0;
        message = in.readInt();
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

    @Override
    public String toString() {
        return "Movie{" +
                "posterPath='" + posterPath + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overviewText='" + overviewText + '\'' +
                ", localizedTitle='" + localizedTitle + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }

    public boolean isLoader() {
        return empty;
    }

    public boolean isError() {
        return error;
    }

    public void displayLoader() {
        error = false;
    }

    public void displayError(int message) {
        this.message = message;
        error = true;
    }

    public int getErrorMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(originalTitle);
        dest.writeString(overviewText);
        dest.writeString(localizedTitle);
        dest.writeDouble(rating);
        dest.writeByte((byte) (empty ? 1 : 0));
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeInt(message);
    }
}
