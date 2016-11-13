package ru.ifmo.droid2016.tmdb.loader;

public enum ResultType {

    OK,

    NO_INTERNET,

    ERROR;

    @Override
    public String toString() {
        switch (this) {
            case OK:
                return "Полёт нормальный";
            case NO_INTERNET:
                return "Интернет не завезли";
            default:
                return "Что-то сломалось";
        }
    }
}
