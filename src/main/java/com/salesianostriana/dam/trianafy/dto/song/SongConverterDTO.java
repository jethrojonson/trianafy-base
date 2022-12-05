package com.salesianostriana.dam.trianafy.dto.song;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.Integer.parseInt;

@Component
@RequiredArgsConstructor
public class SongConverterDTO {
    public Song newSongDtoToSong (NewSongDTO newSongDTO, Artist artist){
        Song song = new Song();
        song.setArtist(artist);
        song.setYear(String.valueOf(newSongDTO.getYear()));
        song.setTitle(newSongDTO.getTitle());
        song.setAlbum(newSongDTO.getAlbum());
        return song;
    }

    public GetSongDTO songToGetSongDTO (Song song){
        GetSongDTO getDTO = new GetSongDTO();
        getDTO.setId(song.getId());
        getDTO.setArtist(song.getArtist()!=null?song.getArtist().getName():null);
        getDTO.setYear(parseInt(song.getYear()));
        getDTO.setAlbum(song.getAlbum());
        getDTO.setTitle(song.getTitle());
        return getDTO;
    }

}
