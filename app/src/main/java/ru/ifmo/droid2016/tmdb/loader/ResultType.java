package ru.ifmo.droid2016.tmdb.loader;

/**
 * Три возможных результат процесса загрузки данных.
 */
public enum ResultType {

    /**
     * Данные успешно загружены.
     */
    OK,

    /**
     * Данные не загружены из-за отсутствия интернета.
     */
    NO_INTERNET,

    /**
     * Данные не загружены по другой причине.
     */
    ERROR;

    @Override
    public String toString() {
        switch (this) {
            case OK:
                return "Access to the Interner";
            case NO_INTERNET:
                return "No access to the Internet";
            default:
                return "ERROR";
        }
    }
}
