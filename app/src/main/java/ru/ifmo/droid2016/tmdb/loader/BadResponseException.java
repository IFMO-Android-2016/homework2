package ru.ifmo.droid2016.tmdb.loader;

/**
 * Kind of bad, incorrect or unexpected response from API.
 */
class BadResponseException extends Exception {

    BadResponseException(String message) {
        super(message);
    }

}
