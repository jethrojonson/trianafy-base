package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.playlist.*;
import com.salesianostriana.dam.trianafy.dto.song.GetSongDTO;
import com.salesianostriana.dam.trianafy.dto.song.SongConverterDTO;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
@Tag(name = "Playlist Controller", description = "Controlador de las listas de reproducción")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final SongService songService;
    private final PlaylistConverterDTO playlistConverterDTO;
    private final SongConverterDTO songConverterDTO;


    @Operation(summary = "Este endopoint añade una nueva playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha añadido correctamente la playlist",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetCreatePlaylistDTO.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id" : 1,
                                                "name" : "Nombre de la playlist",
                                                "description" : "Descripción de la playlist"
                                            }
                                            """
                            )}
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Petición erronéa",
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<GetCreatePlaylistDTO> addNewPlaylist(@RequestBody NewPlaylistDTO newPlaylist) {
        Playlist toAdd = playlistConverterDTO.newPlaylistDtoToPlaylist(newPlaylist);
        if (toAdd.getName() != null && toAdd.getName() != "") {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(playlistConverterDTO
                            .playlistToGetCreatedPlayListDTO(playlistService.add(toAdd)));
        } else
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Este endpoint muestra todas playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado playlists",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetPlaylistDTO.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id" : 1,
                                                "name" : "Nombre de la playlist",
                                                "numberOfSongs" : 3
                                            }
                                            """
                            )}
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado playlists",
                    content = @Content
            )
    })
    @GetMapping("/")
    public ResponseEntity<List<GetPlaylistDTO>> getAllPlaylists() {
        List<Playlist> result = playlistService.findAll();
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(
                    result.stream()
                            .map(playlistConverterDTO::playlistToGetPlaylistDTO)
                            .toList()
            );

    }

    @Operation(summary = "Este endpoint muestra una playlist en detalle por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado la playlist",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetPlaylistDetailDTO.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id" : 1,
                                                "name" : "Nombre de la playlist",
                                                "description" : "Descripción de la playlist",
                                                "songs" : [
                                                    {
                                                        "id" : 1,
                                                        "title" : "Nombre de la canción",
                                                        "artist" : "Nombre del artista",
                                                        "album" : "The Album",
                                                        "year" : 2000
                                                    }
                                                ]
                                            }
                                            """
                            )}
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado playlists",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetPlaylistDetailDTO> getPlaylistDetailsById(@PathVariable Long id) {
        Optional<Playlist> result = playlistService.findById(id);
        if (result.isPresent())
            return ResponseEntity.ok(playlistConverterDTO.playlistToGetPlaylistDetalisDTO(result.get()));
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Este endopoint actualiza la información de una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha actualizado correctamente la playlist",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetCreatePlaylistDTO.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id" : 1,
                                                "name" : "Nombre de la playlist",
                                                "description" : "Descripción de la playlist"
                                            }
                                            """
                            )}
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la playlist",
                    content = @Content
            ),
            @ApiResponse(responseCode = "400",
                    description = "Petición erronéa",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetCreatePlaylistDTO> updatePlaylistById(@PathVariable Long id, @RequestBody NewPlaylistDTO newPlaylist) {
        Optional<Playlist> result = playlistService.findById(id);
        if (result.isPresent()) {
            result.get().setName(newPlaylist.getName());
            result.get().setDescription(newPlaylist.getDescription());
            return ResponseEntity.ok(
                    playlistConverterDTO.playlistToGetCreatedPlayListDTO(
                            playlistService.edit(result.get())
                    )
            );
        } else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Este endopoint borra una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado la playlist",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        Optional<Playlist> result = playlistService.findById(id);
        if (result.isPresent())
            playlistService.delete(result.get());
        return ResponseEntity.noContent().build();
    }

    //FALTA DOCU
    @PostMapping("/{listId}/song/{songId}")
    public ResponseEntity<GetPlaylistDetailDTO> addSongToAPlaylist(@PathVariable Long listId, @PathVariable Long songId) {
        Optional<Playlist> listResult = playlistService.findById(listId);
        Optional<Song> songResult = songService.findById(songId);
        if (listResult.isPresent() && songResult.isPresent()) {
            listResult.get().addSong(songResult.get());
            return ResponseEntity
                    .ok(
                            playlistConverterDTO
                                    .playlistToGetPlaylistDetalisDTO(
                                            playlistService
                                                    .edit(listResult.get())));
        } else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/song")
    public ResponseEntity<GetPlaylistDetailDTO> getAllSongsInPlaylist(@PathVariable Long id) {
        Optional<Playlist> result = playlistService.findById(id);
        if (result.isPresent())
            return ResponseEntity.ok(playlistConverterDTO.playlistToGetPlaylistDetalisDTO(result.get()));
        else
            return ResponseEntity.notFound().build();
    }

    //FALTA DOCU
    @GetMapping("/{listId}/song/{songId}")
    public ResponseEntity<List<GetSongDTO>> getASongInAPlaylist(@PathVariable Long listId,@PathVariable Long songId){
        Optional <Playlist> list = playlistService.findById(listId);
        Optional <Song> song = songService.findById(songId);
        List <GetSongDTO> songsDTO;
        if (list.isPresent()&& song.isPresent()){
            songsDTO = list.get().getSongs().stream()
                    .filter(s -> s.getId() == song.get().getId())
                    .map(songConverterDTO::songToGetSongDTO)
                    .toList();
            if(songsDTO.isEmpty())
                return ResponseEntity.notFound().build();
            else
                return ResponseEntity.ok(songsDTO);
        }else
            return ResponseEntity.notFound().build();
    }

    //FALTA DOCU
    @DeleteMapping("/{listId}/song/{songId}")
    public ResponseEntity<?> deleteSongFromPlaylist(@PathVariable Long listId, @PathVariable Long songId){
        Optional <Playlist> list = playlistService.findById(listId);
        Optional <Song> song = songService.findById(songId);
        List<Song> songsInList;
        if (list.isPresent()&& song.isPresent()){
            songsInList = list.get().getSongs().stream()
                    .filter(s->s.getId()==songId)
                    .toList();
            if(songsInList.isEmpty())
                return ResponseEntity.noContent().build();
            else {
                list.get().getSongs().removeAll(songsInList);
                playlistService.edit(list.get());
                return ResponseEntity.noContent().build();
            }
        }
        else
            return ResponseEntity.noContent().build();
    }
}
