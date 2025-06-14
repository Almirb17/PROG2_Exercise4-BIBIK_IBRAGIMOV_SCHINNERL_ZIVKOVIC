package at.ac.fhcampuswien.fhmdb.state_pattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public interface SortState {
    void sort(List<Movie> movies);
    SortState next();
}
