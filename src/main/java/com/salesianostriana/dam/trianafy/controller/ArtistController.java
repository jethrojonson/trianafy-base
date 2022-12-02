package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;

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

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
@Tag(name = "Artist Controller", description = "Controlador de los artistas")
public class ArtistController {

    private final ArtistService artistService;
    private final SongService songService;

    @Operation(summary = "Este endpoint añade un nuevo artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "El artista se ha añadido satisfactoriamente",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "name": "Nombre artista"
                                            }
                                            """
                            )}
                    )}
            ),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos enviados",
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<Artist> createNewArtist(@RequestBody Artist artist) {
        if (artistService.validateArtist(artist))
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(artistService.add(artist));
        else
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Este endpoint devuelve todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado artistas",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 1,
                                                    "name": "Nombre artista"
                                                },
                                                {
                                                    "id": 2,
                                                    "name": "Nombre artista 2"
                                                }
                                            ]
                                            """
                            )}
                    )
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado artistas",
                    content = @Content
            )
    })
    @GetMapping("/")
    public ResponseEntity<?> getAllArtists() {
        List<Artist> result = artistService.findAll();
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @Operation(summary = "Este endpoint devuelve un artista por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado al artista buscado",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "name" : "Nombre artista"
                                            }
                                            """
                            )

                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado al artista",
                    content = @Content
            )
    })

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtistById(@Parameter(description = "Id del artista", required = true) @PathVariable Long id) {
        Artist result = artistService.findById(id).orElse(null);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    //FALTA DOCU
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnArtist(@Parameter(name = "Id del artista", required = true) @PathVariable Long id, @RequestBody Artist toAdd) {
        Artist old = artistService.findById(id).orElse(null);
        if (old != null) {
            songService.findAll()
                    .stream()
                    .filter(song -> id.equals(song
                            .getArtist()
                            .getId()))
                    .forEach(song -> {
                        song.getArtist().setName(toAdd.getName());
                        songService.edit(song);
                    });
            toAdd.setId(old.getId());
            return ResponseEntity.ok(artistService.add(toAdd));
        } else
            return ResponseEntity.notFound().build();
    }

    //FALTA DOCU
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnArtist(@Parameter(name = "Id del artista", required = true) @PathVariable Long id) {
        Artist result = artistService.findById(id).orElse(null);
        if (result != null) {
            songService.findAll()
                    .stream()
                    .filter(song -> id.equals(song
                            .getArtist()
                            .getId()))
                    .forEach(song -> {
                        song.setArtist(null);
                        songService.edit(song);
                    });
            artistService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();
    }
}
