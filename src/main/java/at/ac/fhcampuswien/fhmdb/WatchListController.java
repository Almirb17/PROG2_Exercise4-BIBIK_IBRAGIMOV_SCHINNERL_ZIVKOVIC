package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer_pattern.Observer;
import at.ac.fhcampuswien.fhmdb.repos.MovieRepository;
import at.ac.fhcampuswien.fhmdb.repos.WatchListRepository;
import at.ac.fhcampuswien.fhmdb.ui.AlertHandler;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class WatchListController implements Initializable, Observer {
    @FXML
    public JFXListView watchListView;

    private WatchListRepository g_wtchlst_repo;
    private MovieRepository g_movie_repo;

    protected ObservableList<Movie> observableWatchlistElements = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState()
    {
        //init repos
        try {
            g_wtchlst_repo = WatchListRepository.getInstance();
        }
        catch(DatabaseException e) {
            AlertHandler.throwErrorAlert("Fehler beim Initialisieren der Watchlist Datenbank aufgetreten:\n" + e.getMessage());
        }
        try {
            g_movie_repo = MovieRepository.getInstance();
        }
        catch(DatabaseException e) {
            AlertHandler.throwErrorAlert("Fehler beim Initialisieren der Movie Datenbank aufgetreten:\n" + e.getMessage());
        }

        //laden der watchlist daten
        try {
            observableWatchlistElements.clear();
            observableWatchlistElements.addAll(g_movie_repo.getWatchlistBasedMovies(g_wtchlst_repo.getWatchlist()));
        }
        catch(DatabaseException e) {
            AlertHandler.throwErrorAlert("Fehler beim Laden der Filme aus der Watchlist:\n" + e.getMessage());
        }
    }

    public void initializeLayout() {
        watchListView.setItems(observableWatchlistElements);
        watchListView.setCellFactory(movieListView -> new WatchlistCell(onRemoveClicked));
    }

    private final ClickEventHandler onRemoveClicked = (clickedItem) ->
    {
        Movie movie = (Movie) clickedItem;
        try {
            g_wtchlst_repo.removeFromWatchlist(new WatchlistMovieEntity(movie.getId()));
            observableWatchlistElements.remove(movie);
        }
        catch (DatabaseException e) {
            AlertHandler.throwErrorAlert("Fehler beim Entfernen des Films aus der Watchlist:\n" + e.getMessage());
        }

    };
    @Override
    public void update(String message) {
        AlertHandler.throwErrorAlert(message);
    }

}
