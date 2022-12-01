package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
@Tag(name="Artist Controller", description = "Controlador de los artistas")
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary = "Este endpoint devuelve todos los artistas")
    @GetMapping("")
    public ResponseEntity<List<Artist>> getAllArtists(){
        List<Artist>result=artistService.findAll();
        if(result.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }
}
