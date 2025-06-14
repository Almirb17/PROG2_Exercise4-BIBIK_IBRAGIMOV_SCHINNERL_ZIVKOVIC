package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class WatchlistCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final JFXButton detailBtn = new JFXButton("Show Details");
    private final VBox layout = new VBox(title, detail, genre);
    private boolean collapsedDetails = true;

    public WatchlistCell(ClickEventHandler onRemoveClicked) {
        super();

        // Styling
        detailBtn.setStyle("-fx-background-color: #f5c518;");
        JFXButton removeBtn = new JFXButton("Remove");
        removeBtn.setStyle("-fx-background-color: #f5c518;");
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic");

        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        HBox buttonBox = new HBox(detailBtn, removeBtn);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(5, 0, 0, 0));
        layout.getChildren().add(buttonBox);

        title.fontProperty().set(title.getFont().font(20));
        detail.setWrapText(true);

        detailBtn.setOnMouseClicked(mouseEvent -> {
            if (collapsedDetails) {
                layout.getChildren().add(getDetails());
                collapsedDetails = false;
                detailBtn.setText("Hide Details");
            } else {
                layout.getChildren().remove(layout.getChildren().size() - 1);
                collapsedDetails = true;
                detailBtn.setText("Show Details");
            }
            setGraphic(layout);
        });

        removeBtn.setOnMouseClicked(mouseEvent -> {
            onRemoveClicked.onClick(getItem());
        });
    }

    private VBox getDetails() {
        VBox details = new VBox();
        Label releaseYear = new Label("Release Year: " + getItem().getReleaseYear());
        Label length = new Label("Length: " + getItem().getLengthInMinutes() + " minutes");
        Label rating = new Label("Rating: " + getItem().getRating() + "/10");

        Label directors = new Label("Directors: " + String.join(", ", getItem().getDirectors()));
        Label writers = new Label("Writers: " + String.join(", ", getItem().getWriters()));
        Label mainCast = new Label("Main Cast: " + String.join(", ", getItem().getMainCast()));

        releaseYear.getStyleClass().add("text-white");
        length.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");
        directors.getStyleClass().add("text-white");
        writers.getStyleClass().add("text-white");
        mainCast.getStyleClass().add("text-white");

        details.getChildren().addAll(releaseYear, rating, length, directors, writers, mainCast);
        return details;
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        if(this.getScene() == null){return;}

        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            String genres = movie.getGenres()
                    .stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            genre.setText(genres);

                detail.setMaxWidth(this.getScene().getWidth() - 30);
            setGraphic(layout);
        }
    }
}
