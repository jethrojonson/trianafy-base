package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
@Tag(name= "Playlist Controller",description = "Controlador de las listas de reproducción")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Operation(summary = "Este endpoint devuelve todas las listas de reproducción")
    @GetMapping("")
    public ResponseEntity <List<Playlist>> getAllPlaylists (){
        List<Playlist>result=playlistService.findAll();
        if (result.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }
}
