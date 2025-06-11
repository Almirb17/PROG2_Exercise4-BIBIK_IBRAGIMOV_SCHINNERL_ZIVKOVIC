package at.ac.fhcampuswien.fhmdb.state_pattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class NotSortedState implements SortState {
    @Override
    public void sort(List<Movie> movies) {
        //bleibt wie gewohnt
    }

    @Override
    public SortState next() {
        return new SortAscState();
    }
}
