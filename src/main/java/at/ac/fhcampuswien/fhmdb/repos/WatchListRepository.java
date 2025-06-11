package at.ac.fhcampuswien.fhmdb.repos;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.List;

public class WatchListRepository {
    static Dao<WatchlistMovieEntity, String> dao;

    public WatchListRepository() throws DatabaseException {
        this.dao = DatabaseManager.getInstance().getWatchListDao();
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
        }
        catch (SQLException e) {
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
            dao.create(wme);
        }
        catch (SQLException e) {
            throw new DatabaseException("Fehler beim Hinzufügen der Watchlist " + wme.toString(), e);
        }
    }




}
