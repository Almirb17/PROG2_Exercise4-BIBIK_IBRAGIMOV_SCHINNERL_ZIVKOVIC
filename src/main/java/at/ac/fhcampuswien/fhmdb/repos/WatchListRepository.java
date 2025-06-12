package at.ac.fhcampuswien.fhmdb.repos;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.observer_pattern.Observer;
import at.ac.fhcampuswien.fhmdb.observer_pattern.Subject;
import com.j256.ormlite.dao.Dao;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchListRepository implements Subject {

    private List<Observer> observers = new ArrayList<>();

    static Dao<WatchlistMovieEntity, String> dao;
    private static WatchListRepository instance;

    private WatchListRepository() throws DatabaseException {
        dao = DatabaseManager.getInstance().getWatchListDao();
    }

    public static WatchListRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new WatchListRepository();
        }
        return instance;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return dao.queryForAll();
        }
        catch (SQLException e) {
            throw new DatabaseException("Fehler beim Abruf der gesamten Watchlist", e);
        }
    }

    public void removeFromWatchlist(WatchlistMovieEntity wme) throws DatabaseException {
        try {
            dao.delete(wme);
            notifyObservers("Film erfolgreich von Watchlist entfernt");
        }
        catch (SQLException e) {
            notifyObservers(e.getMessage());
            throw new DatabaseException("Fehler beim Löschen der Watchlist " + wme.toString(), e);
        }
    }

    public void clearWatchlist() throws DatabaseException {
        try {
            dao.deleteBuilder().delete();
        }
        catch (SQLException e) {
            throw new DatabaseException("Fehler beim Löschen der gesamten Watchlist", e);
        }
    }

    public void addToWatchlist(WatchlistMovieEntity wme) throws DatabaseException {
        try {
            // Prüfen, ob der Film bereits existiert
            WatchlistMovieEntity existing = dao.queryForId(wme.getApiId());

            if (existing != null) {
                notifyObservers("Film ist bereits in der Watchlist.");
            } else {
                dao.create(wme);
                notifyObservers("Film erfolgreich zur Watchlist hinzugefügt.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Hinzufügen zur Watchlist: " + wme.toString(), e);
        }
    }


    //for observer pattern
    @Override
    public void registerObserver(Observer o) {
        boolean alreadyRegistered = observers.stream()
                .anyMatch(existing -> existing.getClass().equals(o.getClass()));

        if (!alreadyRegistered) {
            observers.add(o);
        }
    }

    @Override
    public void removeOberserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for(Observer o : observers) {
            o.update(message);
        }
    }
}
