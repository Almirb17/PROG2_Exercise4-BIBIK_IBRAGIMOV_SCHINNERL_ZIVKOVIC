package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Genre;

import java.util.UUID;

public class MovieApiRequestHandler {
    private static final String URL = "http://prog2.fh-campuswien.ac.at/movies";
    private final StringBuilder urlBuilder = new StringBuilder(URL);
    private boolean hasParams = false;

    private void appendParam(String key, String value) {
        if (!hasParams) {
            urlBuilder.append("?");
            hasParams = true;
        } else {
            urlBuilder.append("&");
        }
        urlBuilder.append(key).append("=").append(value);
    }

    public String build(){
        return urlBuilder.toString();
    }

    public MovieApiRequestHandler query(String query) {
        if (query != null && !query.isEmpty()) {
            appendParam("query", query);
        }
        return this;
    }

    public MovieApiRequestHandler genre(Genre g) {
        if (g != null) {
            appendParam("genre", g.toString());
        }
        return this;
    }

    public MovieApiRequestHandler releaseYear(String year) {
        if (year != null && !year.isEmpty()) {
            appendParam("releaseYear", year);
        }
        return this;
    }

    public MovieApiRequestHandler ratingFrom(String rating) {
        if (rating != null && !rating.isEmpty()) {
            appendParam("ratingFrom", rating);
        }
        return this;
    }

    public MovieApiRequestHandler withId(UUID id) {
        if (id != null) {
            urlBuilder.append("/").append(id);
        }
        return this;
    }
}
