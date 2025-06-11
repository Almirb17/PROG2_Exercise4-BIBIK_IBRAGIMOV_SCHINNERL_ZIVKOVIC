package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager {
    public static final String DB_URL = "jdbc:h2:file:./db/moviedb";
    public static final String DB_USER = "ms";
    public static final String DB_PASSWORD = "1234";

    private static ConnectionSource connectionSource;
    private static DatabaseManager instance;

    private static Dao<MovieEntity, String> movieDao;
    private static Dao<WatchlistMovieEntity, String> watchListDao;

    //private --> can not use "new" for instance
    private DatabaseManager() throws DatabaseException {
        createConnectionSource();
        createDaos();
        createTables();
    }

    //getter
    public static DatabaseManager getInstance() throws DatabaseException {
        if(instance == null){
            instance = new DatabaseManager();
        }

        return instance;
    }

    public Dao<MovieEntity, String> getMovieDao()
    {
        return movieDao;
    }

    public Dao<WatchlistMovieEntity, String> getWatchListDao()
    {
        return watchListDao;
    }

    //private methods
    private static void createDaos() throws DatabaseException
    {
        try {
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
        }
        catch (SQLException e) {
            throw new DatabaseException("Fehler beim Erstellen des Dao der Movie - Tabelle", e);
        }

        try {
            watchListDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
        }
        catch (SQLException e) {
            throw new DatabaseException("Fehler beim Erstellen des Dao der Watchlist - Tablle - Tabelle", e);
        }


    }

    private static void createTables() throws DatabaseException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Erstellen der Movie - Tabelle", e);
        }

        try {
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Fehler beim Erstellen der Watchlist - Tabelle", e);
        }
    }

    private static void createConnectionSource() throws DatabaseException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, DB_USER, DB_PASSWORD);
        }catch (SQLException e) {
            throw new DatabaseException("Fehler beim Anlegen der ConnectionSource", e);
        }
    }
}
