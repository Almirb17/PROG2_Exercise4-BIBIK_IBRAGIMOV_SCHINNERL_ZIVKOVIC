package at.ac.fhcampuswien.fhmdb.state_pattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;
import java.util.List;

public class SortDescState implements SortState {

    @Override
    public void sort(List<Movie> movies) {
        movies.sort(Comparator.comparing(Movie::getTitle).reversed());
    }

    @Override
    public SortState next() {
        return new SortAscState();
    }
}
