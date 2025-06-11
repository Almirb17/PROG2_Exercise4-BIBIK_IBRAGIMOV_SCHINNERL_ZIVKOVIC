package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.repos.MovieRepository;
import at.ac.fhcampuswien.fhmdb.repos.WatchListRepository;
import at.ac.fhcampuswien.fhmdb.ui.AlertHandler;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class MovieController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortBtn;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    protected SortedState sortedState = SortedState.NONE;
    private WatchListRepository g_wtchlst_repo;
    private MovieRepository g_movie_repo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillObservableMovieList(null, null, null, null);
        initializeLayout();
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked)); // apply custom cells to the listview

        // genre combobox
        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        // year combobox
        releaseYearComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 1900 to 2023
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);    // add all years to the combobox
        releaseYearComboBox.setPromptText("Filter by Release Year");

        // rating combobox
        ratingFromComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 0 to 10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);    // add all ratings to the combobox
        ratingFromComboBox.setPromptText("Filter by Rating");
    }

    @FXML
    private void searchBtnClicked(ActionEvent actionEvent) {

        String searchQuery = searchField.getText().trim().toLowerCase();
        String releaseYear = validateComboboxValue(releaseYearComboBox.getSelectionModel().getSelectedItem());
        String ratingFrom = validateComboboxValue(ratingFromComboBox.getSelectionModel().getSelectedItem());
        String genreValue = validateComboboxValue(genreComboBox.getSelectionModel().getSelectedItem());

        Genre genre = null;
        if(genreValue != null) {
            genre = Genre.valueOf(genreValue);
        }

        //from API
        fillObservableMovieList(searchQuery, genre, releaseYear, ratingFrom);

        //sort observable list
        sortMovies(sortedState);
    }

    @FXML
    private void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    private String validateComboboxValue(Object value) {
        if(value != null && !value.toString().equals("No filter")) {
            return value.toString();
        }
        return null;
    }

    private void setMovieObservableList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    public void fillObservableMovieList(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        List<Movie> result = new ArrayList<>();
        boolean api_success = false;

        //init repos
        try {
            g_wtchlst_repo = new WatchListRepository();
        }
        catch(DatabaseException e) {
            AlertHandler.throwAlert("Watchlist Datenbank konnte nicht initialisiert werden: \n" + e.getMessage());

        }
        try {
            g_movie_repo = new MovieRepository();
        }
        catch(DatabaseException e) {
            AlertHandler.throwAlert("Movielist Datenbank konnte nicht initialisiert werden: \n" + e.getMessage());

        }

        //Data from API
        try {
            result = MovieAPI.getAllMovies(searchQuery, genre, releaseYear, ratingFrom);;
            api_success = true;
        }
        catch (MovieApiException apiException) {
            AlertHandler.throwAlert("Movies konnten nicht aus der API geladen werden: \n" +apiException.getMessage());

            try {
                result = g_movie_repo.getAllMovies();
            }catch (DatabaseException databaseException)
            {
                AlertHandler.throwAlert("Movies konnten nicht aus der Datenbank geladen werden: \n" +databaseException.getMessage());
            }
        }

        if(api_success)
        {
            try {
                //delete movies database
                g_movie_repo.clearMovies();

                //insert API ones
                g_movie_repo.addAllMovies(result);
            }
            catch (DatabaseException databaseException)
            {
                AlertHandler.throwAlert("Movies können nicht aus der API in die Datenbank hinzugefügt werden: \n" + databaseException.getMessage());
            }
        }
        //in observable list
        setMovieObservableList(result);
    }

    private void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }

    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) ->
    {
        Movie movie = (Movie) clickedItem;
        try {
            g_wtchlst_repo.addToWatchlist(new WatchlistMovieEntity(movie.getId()));
        }
        catch (DatabaseException e) {
            AlertHandler.throwAlert("Movie konnte nicht hinzugefügt werden:\n " + e.getMessage());
        }
    };
}