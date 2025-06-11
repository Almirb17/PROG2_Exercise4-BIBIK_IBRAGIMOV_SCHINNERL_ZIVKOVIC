package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {

    @DatabaseField(id = true)
    private String apiId;

    public WatchlistMovieEntity() {}

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }

    @Override
    public String toString()
    {
        return "{apiId: " + apiId + "}";
    }

    public String getApiId() {
        return apiId;
    }
}
