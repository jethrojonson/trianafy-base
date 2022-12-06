package com.salesianostriana.dam.trianafy.dto.playlist;

import com.salesianostriana.dam.trianafy.dto.song.GetSongDTO;
import com.salesianostriana.dam.trianafy.dto.song.SongConverterDTO;
import com.salesianostriana.dam.trianafy.model.Playlist;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component @RequiredArgsConstructor
public class PlaylistConverterDTO {

    private final ModelMapper modelMapper;
    private final SongConverterDTO songConverterDTO;

    public Playlist newPlaylistDtoToPlaylist (NewPlaylistDTO newPlaylist){
        return modelMapper.map(newPlaylist,Playlist.class);
    }

    public GetCreatePlaylistDTO playlistToGetCreatedPlayListDTO(Playlist playlist){
        return modelMapper.map(playlist,GetCreatePlaylistDTO.class);
    }

    public GetPlaylistDTO playlistToGetPlaylistDTO(Playlist playlist){
        return new GetPlaylistDTO(
                playlist.getId(),
                playlist.getName(),
                (playlist.getSongs().size())
                );
    }

    public GetPlaylistDetailDTO playlistToGetPlaylistDetalisDTO (Playlist playlist){
        return new GetPlaylistDetailDTO(
                playlist.getId(),
                playlist.getName(),
                playlist.getDescription(),
                playlist.getSongs().stream()
                        .map(songConverterDTO::songToGetSongDTO)
                        .toList()
        );
    }



}
