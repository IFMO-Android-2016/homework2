package ru.ifmo.droid2016.tmdb.loader;

/**
 * Четыре возможных результа процесса загрузки данных.
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
    ERROR,

    /**
     * Нечего больше загружать
     */
    LAST_PAGE
}
