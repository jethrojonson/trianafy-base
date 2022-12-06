package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.song.GetSongDTO;
import com.salesianostriana.dam.trianafy.dto.song.NewSongDTO;
import com.salesianostriana.dam.trianafy.dto.song.SongConverterDTO;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
@Tag(name = "Song Controller", description = "Controlador de las canciones")
public class SongController {

    private final SongService songService;
    private final ArtistService artistService;
    private final SongConverterDTO songConverterDTO;


    @Operation(summary = "Este endpoint añade una canción nueva.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha añadido la canción correctamente",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetSongDTO.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 1
                                                "title":"Nombre cancion"
                                                "artist":"Nombre artista"
                                                "album":"Nombre del albúm"
                                                "year": 1999
                                            }
                                            """
                            )}
                    )}
            ),
            @ApiResponse(responseCode = "400",
                    description = "Petición erronea",
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<GetSongDTO> addNewSong(@RequestBody NewSongDTO newSong) {
        Optional<Artist> artist = artistService.findById(newSong.getArtistID());
        if (artist.isPresent()) {
            Song song = songService.add(songConverterDTO.newSongDtoToSong(newSong, artist.get()));
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(songConverterDTO.songToGetSongDTO(song));
        } else
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Este endpoint devuelve todas las canciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado canciones",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """                                           
                                            [
                                                {
                                                    "id": 1,
                                                    "title": "Song title",
                                                    "artist": "Artist name",
                                                    "album": "The Album",
                                                    "year" : 2000
                                                },
                                                {
                                                    "id":2,
                                                    "title":"Song 2 title",
                                                    "artist":"Artist name",
                                                    "album":"The Album",
                                                    "year" : 1999
                                                }
                                            ]
                                            """

                            )}
                    )}

            ),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado canciones",
                    content = @Content
            )
    })
    @GetMapping("/")
    public ResponseEntity<List<GetSongDTO>> getAllSongs() {
        List<GetSongDTO> result = songService.findAll().stream().map(s -> songConverterDTO.songToGetSongDTO(s)).toList();
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @Operation(summary = "Este endpoint obtiene una cancion por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la canción",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetSongDTO.class)),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id" : 1,
                                                "title" : "Nombre de la canción",
                                                "artist" : "Nombre del artista",
                                                "album" : "The Album",
                                                "year" : 
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontado la canción",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetSongDTO> getASongbyId(@Parameter(description = "ID de la cancion", required = true) @PathVariable Long id) {
        Optional<Song> result = songService.findById(id);
        if (result.isPresent())
            return ResponseEntity.ok(songConverterDTO.songToGetSongDTO(result.get()));
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Este endopoint actualiza una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha actualizado la canción",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetSongDTO.class)),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id" : 1,
                                                "title" : "Nombre de la canción",
                                                "artist" : "Nombre del artista",
                                                "album" : "The Album",
                                                "year" : 2000
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontado la canción",
                    content = @Content
            ),
            @ApiResponse(responseCode = "400",
                    description = "Petición erronéa",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetSongDTO> updateASong(@PathVariable Long id, @RequestBody NewSongDTO updatedSong) {
        Optional<Song> old = songService.findById(id);
        Song toAdd;
        if (old.isPresent()) {
            toAdd = songConverterDTO.newSongDtoToSong(updatedSong, artistService.findById(updatedSong.getArtistID()).get());
            toAdd.setId(old.get().getId());
            return ResponseEntity
                    .ok(
                            songConverterDTO.songToGetSongDTO(songService.add(toAdd))
                    );
        } else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Este endopoint borra una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado la canción",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontado la canción",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteSong (@Parameter(description = "id de la canción", required = true) @PathVariable Long id){
        Optional<Song> result = songService.findById(id);
        if(result.isPresent()) {
            songService.delete(result.get());
            return ResponseEntity.noContent().build();
        }
        else
            return ResponseEntity.notFound().build();

    }


}
