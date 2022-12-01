package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
@Tag(name = "Song Controller", description = "Controlador de las canciones")
public class SongController {

    private final SongService songService;

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

            )
    })
    @GetMapping("")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> result = songService.findAll();
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }
}
