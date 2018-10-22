package nl.han.ica.dea.fedor.controllers;

import nl.han.ica.dea.fedor.datasources.PlaylistDAO;
import nl.han.ica.dea.fedor.datasources.TrackDAO;
import nl.han.ica.dea.fedor.dto.PlaylistDTO;
import nl.han.ica.dea.fedor.dto.PlaylistsDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/playlists")
public class PlaylistController {

    @Inject
    private PlaylistDAO playlistDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allPlaylists() {
        List<PlaylistDTO> all = playlistDAO.findAll();

        PlaylistsDTO playlistsDTO = new PlaylistsDTO();

        all.forEach(playlistDTO -> playlistsDTO.addPlaylist(playlistDTO));

        playlistsDTO.setLength(37);

        return Response.ok(playlistsDTO).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPlaylist(@PathParam("id") int id){
        return Response.ok(playlistDAO.findOne(id)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response editPlaylist(PlaylistDTO playlistDTO, @PathParam("id") int id) {
        return Response.ok(playlistDAO.editPlaylist(playlistDTO, id)).build();
    }

    @GET
    @Path("{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allTracks(@PathParam("id") int id) {
        TrackDAO trackDAO = new TrackDAO();
        return Response.ok(trackDAO.findTracksFromPlaylist(id)).build();
    }
}
