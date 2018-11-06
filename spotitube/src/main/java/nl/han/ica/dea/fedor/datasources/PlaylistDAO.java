package nl.han.ica.dea.fedor.datasources;

import nl.han.ica.dea.fedor.datasources.Properties.DatabaseProperties;
import nl.han.ica.dea.fedor.dto.PlaylistDTO;
import nl.han.ica.dea.fedor.dto.PlaylistsDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaylistDAO {
    private Logger logger = Logger.getLogger(getClass().getName());
    private DatabaseProperties databaseProperties;

    public PlaylistDAO() {
        databaseProperties = new DatabaseProperties();
        tryLoadJdbcDriver(databaseProperties);
    }

    public List<PlaylistDTO> findAll() {
        List<PlaylistDTO> playlists = new ArrayList<>();
        tryFindAll(playlists, "SELECT * FROM playlists");
        return playlists;
    }

    public PlaylistDTO findOne(int id) {
        List<PlaylistDTO> playlists = new ArrayList<>();
        tryFindAll(playlists, "SELECT * from playlists WHERE id = " + id);

        return playlists.get(0);
    }

    private void tryLoadJdbcDriver(DatabaseProperties databaseProperties) {
        try {
            Class.forName(databaseProperties.driver());
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Can't load JDBC Driver " + databaseProperties.driver(), e);
        }
    }

    private void tryFindAll(List<PlaylistDTO> playlists, String query) {
        try {
            Connection connection = DriverManager.getConnection(databaseProperties.connectionURL(), databaseProperties.connectionUSER(), databaseProperties.connectionPASS());
            PreparedStatement statement = connection.prepareStatement(query);

            addNewItemsFromDatabase(playlists, statement);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionURL(), e);
        }
    }

    private void addNewItemsFromDatabase(List<PlaylistDTO> playlists, PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            addNewItemFromResultSet(playlists, resultSet);
        }
    }

    private void addNewItemFromResultSet(List<PlaylistDTO> playlists, ResultSet resultSet) throws SQLException {
        PlaylistDTO playlist = new PlaylistDTO();
        playlist.setId(resultSet.getInt("id"));
        playlist.setName(resultSet.getString("name"));
        playlist.setOwner(resultSet.getBoolean("owner"));

        playlists.add(playlist);
    }

    public PlaylistDTO editPlaylist(PlaylistDTO playlistDTO, int id) {

        String playlistnaam = playlistDTO.getName();
        String query = "UPDATE playlists SET name = '" + playlistnaam + "' WHERE id = " + id;

        try {
            Connection connection = DriverManager.getConnection(databaseProperties.connectionURL(), databaseProperties.connectionUSER(), databaseProperties.connectionPASS());

            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();

            statement.close();
            connection.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionURL(), e);
        }

        return playlistDTO;
    }

    public void deletePlaylist(int id) {
        String query = "DELETE FROM playlists WHERE id = " + id;

        try {
            Connection connection = DriverManager.getConnection(databaseProperties.connectionURL(), databaseProperties.connectionUSER(), databaseProperties.connectionPASS());

            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionURL(), e);
        }
    }

    public void addPlaylist(PlaylistDTO playlistDTO) {
        String query = "INSERT INTO playlists(name, owner) VALUES('" + playlistDTO.getName() + "',1)";

        try {
            Connection connection = DriverManager.getConnection(databaseProperties.connectionURL(), databaseProperties.connectionUSER(), databaseProperties.connectionPASS());

            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();

            statement.close();
            connection.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionURL(), e);
        }
    }

    public int findLength(PlaylistDTO playlistDTO) {
        String query = "SELECT SUM(duration) FROM tracks\n" +
                "INNER JOIN playlist_track on playlist_track.track_id = tracks.id\n" +
                "WHERE playlist_id = "+ playlistDTO.getId();

        int lengte = 0;
        try {
            Connection connection = DriverManager.getConnection(databaseProperties.connectionURL(), databaseProperties.connectionUSER(), databaseProperties.connectionPASS());

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
           while(rs.next()){
               lengte = rs.getInt(1);
           }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error communicating with database " + databaseProperties.connectionURL(), e);
        }
    return lengte;
    }
}
