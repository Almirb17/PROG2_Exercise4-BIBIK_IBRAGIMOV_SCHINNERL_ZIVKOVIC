package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.factory_pattern.FXML_Factory;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;


public class HomeController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger hamburgerNav;

    private boolean isNavCollapsed = true;

    private HamburgerBasicCloseTransition transition;
    private FXML_Factory myFactory;

    public void initialize(){
        initHamburger();
        myFactory = new FXML_Factory();
        setContent("MovieList.fxml");
    }

    private void initHamburger() {
        transition = new HamburgerBasicCloseTransition(hamburgerNav);
        transition.setRate(-1);

        hamburgerNav.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            toggleHamburgerTransitionState();
            toggleMenuDrawer();
        });

        drawer.toBack();
    }

    private void toggleHamburgerTransitionState() {
        transition.setRate(transition.getRate() * -1);
        transition.play();
    }

    public void toggleMenuDrawer() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), drawer);
        if (isNavCollapsed) {
            transition.setByX(130);
            drawer.toFront();
        } else {
            transition.setByX(-130);
            transition.setOnFinished(e -> drawer.toBack());
        }
        transition.play();
        isNavCollapsed = !isNavCollapsed;
    }

    public void setContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/" + fxmlPath));

            //loader should use my controllerFactory
            loader.setControllerFactory(myFactory);

            Parent content = loader.load();
            mainPane.setCenter(content);
        } catch (IOException e) {
            System.err.println("Fehler beim Laden von: " + fxmlPath);
            e.printStackTrace();
        }

        if (!isNavCollapsed) {
            toggleMenuDrawer();
            toggleHamburgerTransitionState();
        }
    }

    @FXML
    public void navigateToMovielist() {
        setContent("MovieList.fxml");
    }

    @FXML
    public void navigateToWatchlist() {
        setContent("WatchList.fxml");
    }
}
