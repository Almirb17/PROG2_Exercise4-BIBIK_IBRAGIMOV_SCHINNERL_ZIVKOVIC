package at.ac.fhcampuswien.fhmdb.state_pattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class SortContext {
    private SortState current;

    public SortContext(){
        this.current = new NotSortedState();
    }

    public void sort(List<Movie> movies){
        current.sort(movies);
        current = current.next();
    }

    public void reset(){
        current = new NotSortedState();
    }

    public void applyCurrent(List<Movie> movies){
        current.sort(movies);
    }
}
