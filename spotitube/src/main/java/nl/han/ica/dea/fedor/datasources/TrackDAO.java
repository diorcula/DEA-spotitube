package nl.han.ica.dea.fedor.datasources;

        import nl.han.ica.dea.fedor.datasources.Properties.DatabaseProperties;
        import nl.han.ica.dea.fedor.dto.TrackDTO;

        import javax.inject.Inject;
        import java.sql.*;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.logging.Level;
        import java.util.logging.Logger;

public class TrackDAO {
    private Logger logger = Logger.getLogger(getClass().getName());
    private DatabaseProperties databaseProperties;

    public TrackDAO() {
        databaseProperties = new DatabaseProperties();
        tryLoadJdbcDriver(databaseProperties);
    }

    private void tryLoadJdbcDriver(DatabaseProperties databaseProperties) {
        try {
            Class.forName(databaseProperties.driver());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Can't load JDBC Driver " + databaseProperties.driver(), e);
        }
    }

    public List<TrackDTO> findAll() {
        List<TrackDTO> tracks = tryFindAll("SELECT * from tracks");
        return tracks;
    }

    public List<TrackDTO> findTracksFromPlaylist(int id) {
       // List<TrackDTO> tracks = new ArrayList<>();

       return tryFindAll(
                "SELECT * " +
                        "FROM playlist_track " +
                        "JOIN tracks ON playlist_track.track_id = tracks.id " +
                        "WHERE playlist_track.playlist_id = " + id);


      //  return tracks;
    }

    private List<TrackDTO> tryFindAll(String query) {
        List<TrackDTO> allTracks;
        try {
            Connection connection = DriverManager.getConnection(databaseProperties.connectionURL(), databaseProperties.connectionUSER(), databaseProperties.connectionPASS());
            PreparedStatement statement = connection.prepareStatement(query);

            allTracks = getAllTracks(statement);

            statement.close();
            connection.close();

        } catch (SQLException e) {
            allTracks = new ArrayList<>();
            logger.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionURL(), e);
        }
        return allTracks;
    }

    private TrackDTO mapToTrackDTO(ResultSet resultSet) throws SQLException {
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(resultSet.getInt("track_id"));
        trackDTO.setTitle(resultSet.getString("title"));
        trackDTO.setPerformer(resultSet.getString("performer"));
        trackDTO.setDuration(resultSet.getInt("duration"));
        trackDTO.setAlbum(resultSet.getString("album"));
        trackDTO.setPlaycount(resultSet.getInt("playcount"));
        trackDTO.setPublication_date(resultSet.getString("publication_date"));
        trackDTO.setDescription(resultSet.getString("description"));
        trackDTO.setOffline_available(resultSet.getBoolean("offline_available"));

        return trackDTO;
    }

    private List<TrackDTO> getAllTracks(PreparedStatement statement) throws SQLException {
        List<TrackDTO> tracks = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            TrackDTO trackDTO = new TrackDTO();
            trackDTO = mapToTrackDTO(resultSet);

            tracks.add(trackDTO);
        }
        return tracks;
    }
}
