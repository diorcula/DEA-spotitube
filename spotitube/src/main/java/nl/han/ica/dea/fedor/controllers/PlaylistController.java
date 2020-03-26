package nl.han.ica.dea.fedor.controllers;

import nl.han.ica.dea.fedor.datasources.PlaylistDAO;
import nl.han.ica.dea.fedor.datasources.TrackDAO;
import nl.han.ica.dea.fedor.dto.PlaylistDTO;
import nl.han.ica.dea.fedor.dto.PlaylistsDTO;
import nl.han.ica.dea.fedor.dto.TrackDTO;
import nl.han.ica.dea.fedor.dto.TracksDTO;
import nl.han.ica.dea.fedor.services.PlaylistService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/playlists")
public class PlaylistController {

    private PlaylistService playlistService;
    private PlaylistDAO playlistDAO;
    private TrackDAO trackDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allPlaylists(@QueryParam("token") String token) {

        System.out.println(playlistDAO.findAll());
        PlaylistsDTO all = playlistService.getAllPlaylists();

        return Response.ok(all).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPlaylist(@PathParam("id") int id) {
        return Response.ok(playlistDAO.findOne(id)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response editPlaylist(PlaylistDTO playlistDTO, @PathParam("id") int id) {

        playlistDAO.editPlaylist(playlistDTO, id);

        List<PlaylistDTO> all = playlistDAO.findAll();
        PlaylistsDTO playlistsDTO = new PlaylistsDTO();
        all.forEach(playlistDTO1 -> playlistsDTO.addPlaylist(playlistDTO1));

        return Response.ok(playlistsDTO).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id) {

        playlistDAO.deletePlaylist(id);

        List<PlaylistDTO> all = playlistDAO.findAll();
        PlaylistsDTO playlistsDTO = new PlaylistsDTO();
        all.forEach(playlistDTO1 -> playlistsDTO.addPlaylist(playlistDTO1));

        return Response.ok(playlistsDTO).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(PlaylistDTO playlistDTO) {

        playlistDAO.addPlaylist(playlistDTO);

        PlaylistsDTO playlistsDTO = new PlaylistsDTO();
        List<PlaylistDTO> all = playlistDAO.findAll();
        all.forEach(playlistDTO1 -> playlistsDTO.addPlaylist(playlistDTO1));

        return Response.ok(playlistsDTO).build();
    }

    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allTracksFromPlaylist(@PathParam("id") int id) {

        TracksDTO tracksDTO = new TracksDTO();
        List<TrackDTO> tracks = trackDAO.findTracksFromPlaylist(id);
        tracks.forEach(trackDTO -> tracksDTO.addTrack(trackDTO));

        return Response.ok(tracksDTO).build();
    }

    @DELETE
    @Path("{id}/tracks/{track_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrackFromPlaylist(@PathParam("id") int id, @PathParam("track_id") int track_id) {

        trackDAO.deleteTrack(id, track_id);

        TracksDTO tracksDTO = new TracksDTO();
        List<TrackDTO> tracks = trackDAO.findTracksFromPlaylist(id);
        tracks.forEach(trackDTO1 -> tracksDTO.addTrack(trackDTO1));

        return Response.ok(tracksDTO).build();
    }

    @POST
    @Path("{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrack(TrackDTO trackDTO, @PathParam("id") int id) {

        trackDAO.addTrack(id, trackDTO);

        TracksDTO tracksDTO = new TracksDTO();
        List<TrackDTO> tracks = trackDAO.findTracksFromPlaylist(id);
        tracks.forEach(trackDTO1 -> tracksDTO.addTrack(trackDTO1));

        return Response.ok(tracksDTO).build();
    }

    @Inject
    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Inject
    public void setPlaylistDAO(PlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }
}
